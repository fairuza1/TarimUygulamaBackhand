package ercankara.uygulamam_backhad.repository;

import ercankara.uygulamam_backhad.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {
    User findByEmailAndPassword(String email,String password);
}
