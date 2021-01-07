package dsbd.project.demo.service;


import dsbd.project.demo.controller.HeartBeatRequest;
import org.springframework.stereotype.Service;

@Service
public class HeartBeatService {

    public String ping(HeartBeatRequest heartBeatRequest){
        return "I RECEIVED THIS REQUEST: " + heartBeatRequest.toString();
    }
}
