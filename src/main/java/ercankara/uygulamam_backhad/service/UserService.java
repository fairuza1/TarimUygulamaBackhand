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
}
