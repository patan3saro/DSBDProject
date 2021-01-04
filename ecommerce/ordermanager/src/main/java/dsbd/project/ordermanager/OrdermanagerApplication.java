package dsbd.project.ordermanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = {"order"})
public class OrdermanagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrdermanagerApplication.class, args);
    }

}
