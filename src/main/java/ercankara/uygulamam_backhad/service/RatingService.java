package ercankara.uygulamam_backhad.service;

import ercankara.uygulamam_backhad.dto.RatingDTO;
import ercankara.uygulamam_backhad.entity.Harvest;
import ercankara.uygulamam_backhad.entity.Rating;
import ercankara.uygulamam_backhad.entity.Sowing;
import ercankara.uygulamam_backhad.repository.HarvestRepository;
import ercankara.uygulamam_backhad.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RatingService {
    private final RatingRepository ratingRepository;
    private final HarvestRepository harvestRepository;

    @Autowired
    public RatingService(RatingRepository ratingRepository, HarvestRepository harvestRepository) {
        this.ratingRepository = ratingRepository;
        this.harvestRepository = harvestRepository;
    }

    // Hasat ID'sine göre değerlendirmeleri bul
    public List<Rating> findByHarvestId(Long harvestId) {
        return ratingRepository.findByHarvestId(harvestId);
    }

    public Rating createRating(RatingDTO ratingDTO) {
        Harvest harvest = harvestRepository.findById(ratingDTO.getHarvestId())
                .orElseThrow(() -> new IllegalArgumentException("Harvest bulunamadı: " + ratingDTO.getHarvestId()));

        Rating rating = new Rating();
        rating.setAmount(ratingDTO.getAmount()); // <<<< BURASI YENİ
        rating.setCategoryRatings(ratingDTO.getCategoryRatings());
        rating.setComment(ratingDTO.getComment());
        rating.setHarvestStatus(ratingDTO.getHarvestStatus());
        rating.setTags(ratingDTO.getTags());
        rating.setHarvest(harvest);
        return ratingRepository.save(rating);
    }




    public List<RatingDTO> getAllRatings() {
        return ratingRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public RatingDTO getRatingById(Long id) {
        Rating rating = ratingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rating not found"));
        return convertToDTO(rating);
    }



    private RatingDTO convertToDTO(Rating rating) {
        RatingDTO ratingDTO = new RatingDTO();
        ratingDTO.setId(rating.getId());
        ratingDTO.setAmount(rating.getAmount());
        ratingDTO.setCategoryRatings(rating.getCategoryRatings());
        ratingDTO.setTags(rating.getTags());
        ratingDTO.setComment(rating.getComment());
        ratingDTO.setHarvestStatus(rating.getHarvestStatus());

        Harvest harvest = rating.getHarvest();
        if (harvest != null) {
            ratingDTO.setHarvestId(harvest.getId());

            Sowing sowing = harvest.getSowing();
            Double plantingAmount = Double.valueOf((sowing != null) ? sowing.getPlantingAmount() : null);
            Double amount = rating.getAmount();

            if (plantingAmount != null && plantingAmount != 0 && amount != null) {
                Double yield = amount / plantingAmount;
                ratingDTO.setYieldPerSquareMeter(yield);
            } else {
                ratingDTO.setYieldPerSquareMeter(null);
            }
        }

        return ratingDTO;
    }

    public void deleteRating(Long id) {
        Rating rating = ratingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Silinecek değerlendirme bulunamadı: " + id));
        ratingRepository.delete(rating);
    }
}
