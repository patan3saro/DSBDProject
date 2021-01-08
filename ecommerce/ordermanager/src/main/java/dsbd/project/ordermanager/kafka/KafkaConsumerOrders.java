package dsbd.project.ordermanager.kafka;

import com.google.gson.Gson;
import dsbd.project.ordermanager.service.OrderService;
import order.FinalOrder;
import order.OrderPaymentNotify;
import order.OrderValidationNotify;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class KafkaConsumerOrders {

    @Autowired
    OrderService orderService;


    @KafkaListener(topics = "${ordersTopic}", groupId = "${kafkaGroup}")
    public void listenOrderTopic(ConsumerRecord<String, String> record){
        String key = record.key();
        if(key.equalsIgnoreCase("order_validation")) {
            OrderValidationNotify orderCompletedNotify = new Gson().fromJson(record.value(), OrderValidationNotify.class);
           /* if(orderCompletedNotify.getStatus()!=0){
               prendere orderid e a partire da ciò effettuare
               la query su finalorder e settare status=abort
            }*/
        }
        if(key.equalsIgnoreCase("order_paid")){
            OrderPaymentNotify orderPaymentNotify = new Gson().fromJson(record.value(), OrderPaymentNotify.class);
            Optional<FinalOrder> finalOrder = orderService.getId(orderPaymentNotify.getOrderId(), orderPaymentNotify.getUserId());
            if(finalOrder.isPresent()){ //se l'ordine con OrderId e userId esiste
                //verifico la amountPaid
                FinalOrder foundFinalOrder = finalOrder.get();
                if(foundFinalOrder.getTotalPrice() == orderPaymentNotify.getAmountPaid()) {
                    foundFinalOrder.setStatus("Paid");
                    orderService.updateOrder(foundFinalOrder);
                }
                //completare modificando con ricerca dell'ordine solo byId (creare metodo in OrderService)
                //quindi qui stesso verificare i casi userId e amountPaid
                //quindi inoltrare i messaggi come produttore su kafka

            }
        }
    }
}
