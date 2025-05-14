package ercankara.uygulamam_backhad.repository;

import ercankara.uygulamam_backhad.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByHarvestId(Long harvestId); // Hasat ID'sine göre değerlendirmeleri bul
    List<Rating> findByHarvest_Sowing_Land_CityAndHarvest_Sowing_Land_DistrictAndHarvest_Sowing_Land_Village(
            String city, String district, String village);
}
