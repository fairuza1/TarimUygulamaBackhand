package ercankara.uygulamam_backhad.controller;

import ercankara.uygulamam_backhad.entity.User;
import ercankara.uygulamam_backhad.repository.UserRepository;
import ercankara.uygulamam_backhad.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Kullanıcı kaydı
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        User savedUser = userService.registerUser(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    // Kullanıcı girişi (basit doğrulama)
    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User user) {
        User foundUser = userService.loginUser(user.getEmail(), user.getPassword());
        return foundUser != null ? new ResponseEntity<>(foundUser, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
