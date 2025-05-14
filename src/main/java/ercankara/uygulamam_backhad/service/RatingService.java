package ercankara.uygulamam_backhad.service;

import ercankara.uygulamam_backhad.dto.RatingDTO;
import ercankara.uygulamam_backhad.entity.Harvest;
import ercankara.uygulamam_backhad.entity.Land;
import ercankara.uygulamam_backhad.entity.Rating;
import ercankara.uygulamam_backhad.entity.Sowing;
import ercankara.uygulamam_backhad.repository.HarvestRepository;
import ercankara.uygulamam_backhad.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
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

    public List<Rating> findByHarvestId(Long harvestId) {
        return ratingRepository.findByHarvestId(harvestId);
    }

    public Rating createRating(RatingDTO ratingDTO) {
        Harvest harvest = harvestRepository.findById(ratingDTO.getHarvestId())
                .orElseThrow(() -> new IllegalArgumentException("Harvest bulunamadı: " + ratingDTO.getHarvestId()));

        Rating rating = new Rating();
        rating.setAmount(ratingDTO.getAmount());
        rating.setCategoryRatings(ratingDTO.getCategoryRatings());
        rating.setComment(ratingDTO.getComment());
        rating.setHarvestStatus(ratingDTO.getHarvestStatus());
        rating.setTags(ratingDTO.getTags());
        rating.setHarvest(harvest);

        // ✅ totalScore hesapla ve set et
        double categoryTotal = 0;
        Map<String, Integer> categoryRatings = ratingDTO.getCategoryRatings();
        if (categoryRatings != null && !categoryRatings.isEmpty()) {
            for (Integer val : categoryRatings.values()) {
                if (val != null) {
                    categoryTotal += (double) val / 5.0;
                }
            }
        }

        int harvestStatusScore = ratingDTO.getHarvestStatus() != null ? ratingDTO.getHarvestStatus() : 0;
        double totalScore = (categoryTotal + harvestStatusScore) / 2.0;
        totalScore = Math.round(totalScore * 100.0) / 100.0;

        rating.setTotalScore(totalScore); // veritabanına kaydedilecek

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
        ratingDTO.setTotalScore(rating.getTotalScore());

        Harvest harvest = rating.getHarvest();
        if (harvest != null) {
            ratingDTO.setHarvestId(harvest.getId());

            Sowing sowing = harvest.getSowing();
            if (sowing != null) {
                // m² başına verim hesapla
                Double plantingAmount = (double) sowing.getPlantingAmount();
                Double amount = rating.getAmount();
                if (plantingAmount != null && plantingAmount != 0 && amount != null) {
                    Double yield = amount / plantingAmount;
                    ratingDTO.setYieldPerSquareMeter(yield);
                }

                // 🌱 Ekim Bilgileri
                ratingDTO.setPlantingDate(sowing.getSowingDate().toString());
                ratingDTO.setPlantingMethod(sowing.getCategoryName()); // plantingMethod yerine categoryName geliyor
                if (sowing.getPlant() != null) {
                    ratingDTO.setPlantName(sowing.getPlant().getName());
                }

                // 🌍 Arazi Bilgileri
                Land land = sowing.getLand();
                if (land != null) {
                    ratingDTO.setLandName(land.getName());
                    ratingDTO.setLandSize(land.getLandSize());
                    String location = land.getCity() + ", " + land.getDistrict() + ", " + land.getVillage();
                    ratingDTO.setLandLocation(location);
                }
            }
        }

        return ratingDTO;
    }


    public void deleteRating(Long id) {
        Rating rating = ratingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Silinecek değerlendirme bulunamadı: " + id));
        ratingRepository.delete(rating);
    }
    public List<RatingDTO> getRecommendations(String city, String district, String village) {
        List<Rating> allRatings = ratingRepository
                .findByHarvest_Sowing_Land_CityAndHarvest_Sowing_Land_DistrictAndHarvest_Sowing_Land_Village(
                        city, district, village);

        // Bitkilere göre grupla
        Map<String, List<Rating>> groupedByPlant = allRatings.stream()
                .collect(Collectors.groupingBy(r -> r.getHarvest().getSowing().getPlant().getName()));

        // Her bitki için ortalama skor ve örnek veriye göre RatingDTO oluştur
        List<RatingDTO> recommendations = groupedByPlant.entrySet().stream()
                .map(entry -> {
                    String plantName = entry.getKey();
                    List<Rating> ratings = entry.getValue();
                    double avgScore = ratings.stream()
                            .mapToDouble(Rating::getTotalScore)
                            .average()
                            .orElse(0.0);
                    avgScore = Math.round(avgScore * 100.0) / 100.0;

                    // Örnek bir rating alıp DTO'ya çevir
                    Rating exampleRating = ratings.get(0);
                    RatingDTO dto = convertToDTO(exampleRating);

                    dto.setPlantName(plantName); // zaten atanıyor ama garanti olsun
                    dto.setTotalScore(avgScore); // ortalama ile override ediyoruz

                    return dto;
                })
                .sorted(Comparator.comparingDouble(RatingDTO::getTotalScore).reversed())
                .collect(Collectors.toList());

        return recommendations;
    }


}
