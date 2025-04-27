package ercankara.uygulamam_backhad.repository;

import ercankara.uygulamam_backhad.entity.Harvest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HarvestRepository extends JpaRepository<Harvest, Long> {
    boolean existsBySowingId(Long sowingId);  // Hasat yapılmış mı kontrolü

}
