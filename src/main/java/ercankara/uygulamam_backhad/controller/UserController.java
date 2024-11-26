package ercankara.uygulamam_backhad.controller;

import ercankara.uygulamam_backhad.entity.User;
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
    public ResponseEntity<String> register(@RequestBody User user) {
        try {
            User savedUser = userService.registerUser(user);
            return new ResponseEntity<>("Kullanıcı başarıyla kaydedildi!", HttpStatus.CREATED);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST); // Hata mesajını response body olarak dönüyoruz.
        }
    }

    // Kullanıcı girişi (basit doğrulama)
    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User user) {
        try {
            User foundUser = userService.loginUser(user.getEmail(), user.getPassword());
            return new ResponseEntity<>(foundUser, HttpStatus.OK);
        } catch (RuntimeException ex) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }

    // Global Exception Handler
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleValidationException(IllegalArgumentException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
