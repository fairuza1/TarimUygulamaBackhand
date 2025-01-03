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
        sowingDTO.setRemainingSize(sowing.getLand().getRemainingSize()); // Burada ekleme

        return sowingDTO;
    }
}
