package dsbd.project.ordermanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
//this annotation helps the ordermanager recognize entities by scanning them
@EntityScan(basePackages = {"order","product","user"})
//the ordermanager is an eureka client: It is registered to the registrationserver
@EnableEurekaClient
public class OrdermanagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrdermanagerApplication.class, args);
    }

}
