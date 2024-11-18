package ercankara.uygulamam_backhad.repository;

import ercankara.uygulamam_backhad.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);  // Email ile kullanıcı bul
    Optional<User> findByEmailAndPassword(String email, String password); // Email ve şifre ile kullanıcı bul
    Optional<User> findByUsername(String username); // Username ile kullanıcı bul
}
