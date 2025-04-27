package ercankara.uygulamam_backhad.service;

import ercankara.uygulamam_backhad.dto.HarvestDTO;
import ercankara.uygulamam_backhad.entity.Harvest;
import ercankara.uygulamam_backhad.entity.Sowing;
import ercankara.uygulamam_backhad.entity.Land;
import ercankara.uygulamam_backhad.repository.HarvestRepository;
import ercankara.uygulamam_backhad.repository.SowingRepository;
import ercankara.uygulamam_backhad.repository.LandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HarvestService {

    private final HarvestRepository harvestRepository;
    private final SowingRepository sowingRepository;
    private final LandRepository landRepository;

    @Autowired
    public HarvestService(HarvestRepository harvestRepository, SowingRepository sowingRepository, LandRepository landRepository) {
        this.harvestRepository = harvestRepository;
        this.sowingRepository = sowingRepository;
        this.landRepository = landRepository;
    }

    public List<HarvestDTO> getAllHarvests() {
        return harvestRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public HarvestDTO createHarvest(HarvestDTO harvestDTO) {
        Harvest harvest = convertToEntity(harvestDTO);
        Harvest savedHarvest = harvestRepository.save(harvest);

        // Ekim ile ilişkili araziyi al
        Sowing sowing = savedHarvest.getSowing();
        Land land = sowing.getLand();  // Hasat edilen araziyi alıyoruz.

        if (land != null) {
            double harvestedSize = sowing.getPlantingAmount();  // Ekim miktarı kadar alan hasat edildi.
            double updatedRemainingSize = land.getRemainingSize() + harvestedSize;  // Kalan alanı artırıyoruz.

            // Kalan alan, toplam araziyi geçemez
            if (updatedRemainingSize > land.getLandSize()) {
                updatedRemainingSize = land.getLandSize(); // Kalan alan toplam arazinin boyutunu geçemez
            }

            land.setRemainingSize(updatedRemainingSize);  // Araziyi güncelliyoruz.
            landRepository.save(land);  // Arazinin güncellenmiş hali kaydediliyor.
        }

        return convertToDTO(savedHarvest);
    }

    public void deleteHarvest(Long id) {
        harvestRepository.deleteById(id);
    }

    public HarvestDTO getHarvestById(Long id) {
        return harvestRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    private HarvestDTO convertToDTO(Harvest harvest) {
        HarvestDTO harvestDTO = new HarvestDTO();
        harvestDTO.setId(harvest.getId());
        harvestDTO.setHarvestDate(harvest.getHarvestDate());

        if (harvest.getSowing() != null) {
            Sowing sowing = harvest.getSowing();
            harvestDTO.setSowingId(sowing.getId());
            harvestDTO.setPlantName(sowing.getPlant() != null ? sowing.getPlant().getName() : null); // Bitki adı
            harvestDTO.setCategoryName(sowing.getCategoryName()); // Kategori adı
            harvestDTO.setLandName(sowing.getLand() != null ? sowing.getLand().getName() : null);   // Arazi adı
            harvestDTO.setPlantingAmount(sowing.getPlantingAmount()); // Ekim miktarı
        }

        return harvestDTO;
    }

    public List<HarvestDTO> getHarvestsByUserId(Long userId) {
        return harvestRepository.findAll().stream()
                .filter(harvest -> harvest.getSowing() != null && harvest.getSowing().getUserId().equals(userId))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private Harvest convertToEntity(HarvestDTO harvestDTO) {
        Harvest harvest = new Harvest();
        harvest.setId(harvestDTO.getId());
        harvest.setHarvestDate(harvestDTO.getHarvestDate());

        if (harvestDTO.getSowingId() != null) {
            Sowing sowing = sowingRepository.findById(harvestDTO.getSowingId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid sowing ID: " + harvestDTO.getSowingId()));
            harvest.setSowing(sowing);
        }

        return harvest;
    }
}
