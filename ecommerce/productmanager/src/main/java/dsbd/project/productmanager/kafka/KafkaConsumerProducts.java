package dsbd.project.productmanager.kafka;

import com.google.gson.Gson;
import dsbd.project.productmanager.data.ProductRepository;
import dsbd.project.productmanager.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import product.Product;
import product.ProductUpdateRequest;

import java.util.Optional;

@Component
public class KafkaConsumerProducts {
    @Autowired
    ProductService productService;

    @KafkaListener(topics = "${kafkaTopic}", groupId = "${kafkaGroup}")
    public void listenProductTopic(String message) {
        if (message != null) {
            ProductUpdateRequest updateRequest = new Gson().fromJson(message, ProductUpdateRequest.class);
            Optional<Product> product = productService.findById(updateRequest.getProductId());
            if (product.isPresent()) {
                Product prod = product.get();
                prod.setQuantity(prod.getQuantity()-updateRequest.getProductQuantity());
                productService.save(prod);
            }
        }
    }
}


