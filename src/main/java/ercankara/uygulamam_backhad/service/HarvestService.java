package ercankara.uygulamam_backhad.service;

import ercankara.uygulamam_backhad.dto.HarvestDTO;
import ercankara.uygulamam_backhad.entity.Harvest;
import ercankara.uygulamam_backhad.entity.Sowing;
import ercankara.uygulamam_backhad.repository.HarvestRepository;
import ercankara.uygulamam_backhad.repository.SowingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HarvestService {
    private final HarvestRepository harvestRepository;
    private final SowingRepository sowingRepository;

    @Autowired
    public HarvestService(HarvestRepository harvestRepository, SowingRepository sowingRepository) {
        this.harvestRepository = harvestRepository;
        this.sowingRepository = sowingRepository;
    }

    public List<HarvestDTO> getAllHarvests() {
        return harvestRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public HarvestDTO createHarvest(HarvestDTO harvestDTO) {
        Harvest harvest = convertToEntity(harvestDTO);
        Harvest savedHarvest = harvestRepository.save(harvest);
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
            harvestDTO.setSowingId(harvest.getSowing().getId());
        }
        return harvestDTO;
    }
    public List<HarvestDTO> getHarvestsByUserId(Long userId) {
        // Kullanıcı ID'sine ait ekimlere göre hasatları filtrele
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
