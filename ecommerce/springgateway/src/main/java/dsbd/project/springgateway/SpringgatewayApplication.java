package dsbd.project.springgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class SpringgatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringgatewayApplication.class, args);
    }

    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p.path("/user/**").uri("http://usermanager:2222"))
                .route(p -> p.path("/product/**").uri("http://productmanager:3333"))
                .route(p -> p.path("/order/**").uri("http://ordermanager:4444")).build();
    }

}
