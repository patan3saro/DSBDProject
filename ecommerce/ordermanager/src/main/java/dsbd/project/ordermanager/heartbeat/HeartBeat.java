package dsbd.project.ordermanager.heartbeat;

import com.netflix.discovery.EurekaClient;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

@Component
public class HeartBeat {

    @Autowired
    EurekaClient eurekaClient;

    private ArrayList<String> serviceToTest = new ArrayList<>();

    @Scheduled(fixedDelayString = "${heartBeatTimeout}")
    public void heartbeat() {
        //Services that we want to ping with heartbeat
        serviceToTest.add("productmanager");
        serviceToTest.add("usermanager");

        // Retrieve Fault detector URL from Eureka
        String HEARTBEAT_FAULT_DETECTOR_URL=eurekaClient.getNextServerFromEureka("heartbeatfaultdetector",false).getHomePageUrl();

        //RestTemplate creation
        RestTemplate restTemplate = new RestTemplate();

        //Header setup
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        for(String service: serviceToTest) {
            //JsonObject Setup
            JSONObject pingHeartBeatJsonObject = new JSONObject();
            pingHeartBeatJsonObject.put("service", service);
            pingHeartBeatJsonObject.put("serviceStatus", "up");
            pingHeartBeatJsonObject.put("dbStatus", "up");

            HttpEntity<String> request = new HttpEntity<String>(pingHeartBeatJsonObject.toString(), httpHeaders);

            String heartBeatStatAsJsonStr = restTemplate.postForObject(HEARTBEAT_FAULT_DETECTOR_URL + "/ping", request, String.class);

            System.out.println(service+" STATUS IS: "+heartBeatStatAsJsonStr);
        }
    }
}
