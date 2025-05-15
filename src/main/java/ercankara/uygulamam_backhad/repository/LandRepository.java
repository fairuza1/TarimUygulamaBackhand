package ercankara.uygulamam_backhad.repository;

import ercankara.uygulamam_backhad.entity.Land;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LandRepository extends JpaRepository<Land, Long> {
    List<Land> findByUserId(Long userId); // Kullanıcıya ait arazileri getir
    long count(); // Toplam arazi sayısını verir
}
