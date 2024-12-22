package ercankara.uygulamam_backhad.repository;

import ercankara.uygulamam_backhad.entity.Harvest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HarvestRepository extends JpaRepository<Harvest, Long> {
    List<Harvest> findBySowing_UserId(Long userId);
}
