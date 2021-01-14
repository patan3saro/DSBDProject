package dsbd.project.ordermanager.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    //topics
    @Value("${kafkaTopic}")
    private String topicName;

    @Value("${ordersTopic}")
    private String ordersTopic;

    @Value("${notificationsTopic}")
    private String notificationsTopic;

    @Value("${loggingTopic}")
    private String loggingTopic;

    @Value("${invoicingTopic}")
    private String invoicingTopic;

    @Bean
    public Map<String, Object> producerConfigs(){
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return props;
    }

    @Bean
    public ProducerFactory<String, String> producerFactory(){
        // this set a singleton of the shared instance of Product
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }
    // a bean for each topic we want
    @Bean
    public NewTopic topic1(){ return TopicBuilder.name(topicName).build(); }

    @Bean
    public NewTopic topic2() { return TopicBuilder.name(ordersTopic).build(); }

    @Bean
    public NewTopic topic3() { return TopicBuilder.name(notificationsTopic).build(); }

    @Bean
    public NewTopic topic4() { return TopicBuilder.name(invoicingTopic).build(); }

    @Bean
    public NewTopic topic5() { return TopicBuilder.name(loggingTopic).build(); }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
