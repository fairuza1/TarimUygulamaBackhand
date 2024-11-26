package ercankara.uygulamam_backhad.service;

import ercankara.uygulamam_backhad.entity.User;
import ercankara.uygulamam_backhad.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Kullanıcı kaydı
    public User registerUser(User user) {
        validateUser(user);
        return userRepository.save(user);
    }

    // Kullanıcı girişi
    public User loginUser(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password)
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));
    }

    // Kullanıcıyı email ile bulma
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User with email " + email + " not found"));
    }

    // Kullanıcıyı ID ile bulma
    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }

    // Kullanıcıyı username ile bulma
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    private void validateUser(User user) {
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email alanı boş olamaz!");
        }

        if (!isValidEmail(user.getEmail())) {
            throw new IllegalArgumentException("Geçerli bir email adresi giriniz!");
        }

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Bu email zaten kullanılıyor!");
        }

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Şifre alanı boş olamaz!");
        }

        if (!isValidPassword(user.getPassword())) {
            throw new IllegalArgumentException("Şifre en az bir büyük harf, bir küçük harf, bir rakam ve bir özel karakter içermelidir!");
        }
    }


    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(emailRegex);
    }

    private boolean isValidPassword(String password) {
        String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
        return password.matches(passwordRegex);
    }
}
