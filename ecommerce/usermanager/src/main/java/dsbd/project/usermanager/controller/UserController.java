package dsbd.project.usermanager.controller;

import dsbd.project.usermanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import user.User;

import java.util.Optional;

@Controller
@RequestMapping(path="/user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping(path="/id/{userId}")
    public @ResponseBody Optional<User> findById(@PathVariable Integer userId){

        return userService.findById(userId);
    }
}
