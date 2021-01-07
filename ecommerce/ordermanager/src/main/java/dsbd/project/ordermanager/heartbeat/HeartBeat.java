package dsbd.project.ordermanager.heartbeat;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class HeartBeat {
    @Scheduled(fixedDelay = 180000)
    public void heartbeat() {
        // Your code is below...
        RestTemplate restTemplate = new RestTemplate();
        String url = "endpoint url";
        String requestJson = "{\"I am alive\":\"App name?\"}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<String>(requestJson,headers);
        String answer = restTemplate.postForObject(url, entity, String.class);
        System.out.println(answer);
}
