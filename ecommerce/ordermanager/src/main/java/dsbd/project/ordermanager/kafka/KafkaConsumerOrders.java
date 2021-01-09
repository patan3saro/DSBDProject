package dsbd.project.ordermanager.kafka;

import com.google.gson.Gson;
import dsbd.project.ordermanager.service.OrderService;
import order.FinalOrder;
import order.OrderPaymentNotify;
import order.OrderValidationNotify;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Optional;

@Component
public class KafkaConsumerOrders {

    @Autowired
    OrderService orderService;

    @Autowired      //quello che facilita la pubblicazione sul topic
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${orderPaidFailureKey}")
    private String orderPaidFailureKey;

    @Value("${loggingTopic}")
    private String loggingTopic;

    @Value("${invoicingTopic}")
    private String invoicingTopic;

    @Value("${notificationsTopic}")
    private String notificationsTopic;

    public void sendMessage(String topic, String key, String message){
        kafkaTemplate.send(topic, key, message);
    }

    @KafkaListener(topics = "${ordersTopic}", groupId = "${kafkaGroup}")
    public void listenOrderTopic(ConsumerRecord<String, String> record){
        String key = record.key();
        if(key.equalsIgnoreCase("order_validation")) {
            OrderValidationNotify orderCompletedNotify = new Gson().fromJson(record.value(), OrderValidationNotify.class);
            if(orderCompletedNotify.getStatus()!=0){
               Optional<FinalOrder> finalOrder = orderService.getOrder(orderCompletedNotify.getOrderId());
               if(finalOrder.isPresent()){
                   FinalOrder foundFinalOrder = finalOrder.get();
                   foundFinalOrder.setStatus("Abort");
                   orderService.updateOrder(foundFinalOrder);
               }
            }
        }
        if(key.equalsIgnoreCase("order_paid")){
            OrderPaymentNotify orderPaymentNotify = new Gson().fromJson(record.value(), OrderPaymentNotify.class);
            Optional<FinalOrder> finalOrder = orderService.getOrder(orderPaymentNotify.getOrderId());
            if(finalOrder.isPresent()){ //se l'ordine con OrderId
                FinalOrder foundFinalOrder = finalOrder.get();
                //se userId esiste
                if(foundFinalOrder.getUser().getId() == orderPaymentNotify.getUserId()) {
                    //verifico la amountPaid
                    if (new BigDecimal(foundFinalOrder.getTotalPrice()).compareTo(new BigDecimal(orderPaymentNotify.getAmountPaid()))!=0) {
                        foundFinalOrder.setStatus("Abort");
                        orderService.updateOrder(foundFinalOrder);
                        //(1)WRONG_AMOUNT_PAID
                        HashMap myValues = new HashMap<>();
                        myValues.put("error", "WRONG_AMOUNT_PAID");
                        orderPaymentNotify.setExtraArgs(myValues);
                        sendMessage(loggingTopic, orderPaidFailureKey, new Gson().toJson(orderPaymentNotify));
                    } else {
                        foundFinalOrder.setStatus("Paid");
                        orderService.updateOrder(foundFinalOrder);
                        sendMessage(notificationsTopic, key, record.value());
                        sendMessage(invoicingTopic, key, record.value());
                    }
                }
                else {
                    foundFinalOrder.setStatus("Abort");
                    orderService.updateOrder(foundFinalOrder);
                    //(1)ORDER_NOT_FOUND
                    HashMap myValues = new HashMap<>();
                    myValues.put("error", "ORDER_NOT_FOUND");
                    orderPaymentNotify.setExtraArgs(myValues);
                    sendMessage(loggingTopic, orderPaidFailureKey, new Gson().toJson(orderPaymentNotify));
                }
            }
            else {
                //(1)ORDER_NOT_FOUND
                HashMap myValues = new HashMap<>();
                myValues.put("error", "ORDER_NOT_FOUND");
                orderPaymentNotify.setExtraArgs(myValues);
                sendMessage(loggingTopic, orderPaidFailureKey, new Gson().toJson(orderPaymentNotify));
            }
        }
    }
}
