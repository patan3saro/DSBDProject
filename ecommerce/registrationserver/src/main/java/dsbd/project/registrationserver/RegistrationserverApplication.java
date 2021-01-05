package dsbd.project.registrationserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
//Per abilitare il registration server come eureka server
@EnableEurekaServer
public class RegistrationserverApplication {

    public static void main(String[] args) {
        SpringApplication.run(RegistrationserverApplication.class, args);
    }

}
