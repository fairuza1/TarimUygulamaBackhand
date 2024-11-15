package ercankara.uygulamam_backhad.controller;

import ercankara.uygulamam_backhad.entity.User;
import ercankara.uygulamam_backhad.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public User Register(@RequestBody User user) {
        return userRepository.save(user);
    }
    @PostMapping("/login")
    public User Login(@RequestBody User user) {
        User oldUSer = userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword());
        return oldUSer;
    }
}