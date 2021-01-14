package dsbd.project.demo.controller;

import dsbd.project.demo.service.HeartBeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("")
public class HeartBeatController {

    @Autowired
    HeartBeatService heartBeatService;
    // this controller is useful only in order to test /ping request
    @PostMapping(path="/ping")
    public @ResponseBody String ping(@RequestBody HeartBeatRequest heartBeatRequest)
    {
        return heartBeatService.ping(heartBeatRequest);
    }

}
