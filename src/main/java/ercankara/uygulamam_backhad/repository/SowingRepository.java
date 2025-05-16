package ercankara.uygulamam_backhad.repository;

import ercankara.uygulamam_backhad.entity.Sowing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SowingRepository extends JpaRepository<Sowing, Long> {

    List<Sowing> findByLandId(Long landId);  // Araziye göre ekimleri al
    List<Sowing> findByUserId(Long userId);  // Kullanıcıya göre ekimleri al
    List<Sowing> findTop5ByUserIdOrderBySowingDateDesc(Long userId);
}
