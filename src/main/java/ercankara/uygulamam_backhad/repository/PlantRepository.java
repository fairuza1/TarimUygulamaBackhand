package ercankara.uygulamam_backhad.repository;

import ercankara.uygulamam_backhad.entity.Plant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlantRepository extends JpaRepository<Plant, Long> {
    List<Plant> findByCategoryId(Long categoryId);
}
