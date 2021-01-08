package dsbd.project.ordermanager.kafka;

import com.google.gson.Gson;
import dsbd.project.ordermanager.service.OrderService;
import order.OrderValidationNotify;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

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
    }
}
