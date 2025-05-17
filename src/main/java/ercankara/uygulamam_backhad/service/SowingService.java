package ercankara.uygulamam_backhad.service;

import ercankara.uygulamam_backhad.controller.GlobalExceptionHandler;
import ercankara.uygulamam_backhad.dto.SowingDTO;
import ercankara.uygulamam_backhad.entity.Sowing;
import ercankara.uygulamam_backhad.entity.Land;
import ercankara.uygulamam_backhad.entity.Plant;
import ercankara.uygulamam_backhad.repository.SowingRepository;
import ercankara.uygulamam_backhad.repository.LandRepository;
import ercankara.uygulamam_backhad.repository.PlantRepository;
import ercankara.uygulamam_backhad.repository.UserRepository;
import ercankara.uygulamam_backhad.repository.HarvestRepository;  // HarvestRepository ekleniyor
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SowingService {

    private final SowingRepository sowingRepository;
    private final LandRepository landRepository;
    private final PlantRepository plantRepository;
    private final UserRepository userRepository;
    private final HarvestRepository harvestRepository;  // HarvestRepository enjekte ediliyor

    public List<SowingDTO> getSowingsByLandId(Long landId) {
        List<Sowing> sowings = sowingRepository.findByLandId(landId);
        return sowings.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<SowingDTO> getSowingsByUserId(Long userId) {
        List<Sowing> sowings = sowingRepository.findByUserId(userId);
        return sowings.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public SowingDTO createSowing(SowingDTO sowingDTO) {
        // Araziyi bulma
        Land land = landRepository.findById(sowingDTO.getLandId())
                .orElseThrow(() -> new RuntimeException("Land not found"));

        // Bitkiyi bulma
        Plant plant = plantRepository.findById(sowingDTO.getPlantId())
                .orElseThrow(() -> new RuntimeException("Plant not found"));

        if (land.getRemainingSize() < sowingDTO.getPlantingAmount()) {
            throw new GlobalExceptionHandler.InsufficientLandSizeException("Yetersiz alan! Kalan alan: " + land.getRemainingSize());
        }

        // Yeni ekim kaydı oluşturma
        Sowing sowing = new Sowing();
        sowing.setLand(land);
        sowing.setPlant(plant);
        sowing.setPlantingAmount(sowingDTO.getPlantingAmount());
        sowing.setSowingDate(sowingDTO.getSowingDate());

        // Kategori bilgisi
        sowing.setCategoryId(sowingDTO.getCategoryId());
        sowing.setCategoryName(sowingDTO.getCategoryName());
        sowing.setUserId(sowingDTO.getUserId());

        // Arazi kalan alanını güncelleme
        double updatedRemainingSize = land.getRemainingSize() - sowingDTO.getPlantingAmount();
        land.setRemainingSize(updatedRemainingSize);
        landRepository.save(land); // Güncellenmiş araziyi kaydet

        // Ekim kaydını kaydetme ve dönüş
        Sowing savedSowing = sowingRepository.save(sowing);
        return convertToDTO(savedSowing);
    }

    private SowingDTO convertToDTO(Sowing sowing) {
        SowingDTO sowingDTO = new SowingDTO();
        sowingDTO.setId(sowing.getId());
        sowingDTO.setLandId(sowing.getLand().getId());
        sowingDTO.setLandName(sowing.getLand().getName());
        sowingDTO.setPlantName(sowing.getPlant().getName());
        sowingDTO.setPlantingAmount(sowing.getPlantingAmount());
        sowingDTO.setSowingDate(sowing.getSowingDate());
        sowingDTO.setCategoryId(sowing.getCategoryId());
        sowingDTO.setUserId(sowing.getUserId());
        sowingDTO.setCategoryName(sowing.getCategoryName());
        sowingDTO.setRemainingSize(sowing.getLand().getRemainingSize());

        boolean isHarvested = harvestRepository.existsBySowingId(sowing.getId());
        sowingDTO.setHarvested(isHarvested);  // DTO'ya aktar

        return sowingDTO;
    }

    public void deleteSowing(Long id) {
        Sowing sowing = sowingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sowing not found"));

        Land land = sowing.getLand();
        double plantingAmount = sowing.getPlantingAmount();

        // Hasat yapılmış mı kontrol et
        boolean isHarvested = harvestRepository.existsBySowingId(sowing.getId());

        if (!isHarvested) {
            // Eğer hasat yapılmamışsa, ekilen alanı kalan alana ekle
            double newRemainingSize = land.getRemainingSize() + plantingAmount;

            // Kalan alan, toplam alanı geçemez
            if (newRemainingSize > land.getLandSize()) {
                newRemainingSize = land.getLandSize();
            }

            land.setRemainingSize(newRemainingSize);
            landRepository.save(land); // Güncellenmiş araziyi kaydet
        }

        sowingRepository.delete(sowing);
    }
    public double calculateTotalCultivatedAreaByUser(Long userId) {
        // İlgili kullanıcıya ait tüm ekimleri getir
        List<Sowing> sowings = sowingRepository.findByUserId(userId);

        // Hasat edilmemiş ekimleri filtrele
        List<Sowing> unharvestedSowings = sowings.stream()
                .filter(sowing -> !harvestRepository.existsBySowingId(sowing.getId()))
                .toList();

        // Sadece hasat edilmemiş ekimlerin toplam alanını hesapla
        return unharvestedSowings.stream()
                .mapToDouble(Sowing::getPlantingAmount)
                .sum();
    }

    public List<SowingDTO> getRecentSowingsByUserId(Long userId) {
        List<Sowing> sowings = sowingRepository.findTop5ByUserIdOrderBySowingDateDesc(userId);
        return sowings.stream().map(this::convertToDTO).collect(Collectors.toList());
    }



}
