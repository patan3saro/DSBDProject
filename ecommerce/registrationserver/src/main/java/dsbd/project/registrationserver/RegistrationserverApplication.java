package dsbd.project.registrationserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
//In order to enable registration server as eureka server
@EnableEurekaServer
public class RegistrationserverApplication {

    public static void main(String[] args) {
        SpringApplication.run(RegistrationserverApplication.class, args);
    }

}
