package ercankara.uygulamam_backhad.service;

import ercankara.uygulamam_backhad.dto.SowingDTO;
import ercankara.uygulamam_backhad.entity.Land;
import ercankara.uygulamam_backhad.entity.Sowing;
import ercankara.uygulamam_backhad.repository.LandRepository;
import ercankara.uygulamam_backhad.repository.SowingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SowingService {
    private final SowingRepository sowingRepository;
    private final LandRepository landRepository;

    public List<SowingDTO> getSowingsByLandId(Long landId) {
        return sowingRepository.findByLandId(landId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public SowingDTO createSowing(SowingDTO sowingDTO) {
        Optional<Land> land = landRepository.findById(sowingDTO.getLandId());
        if (land.isEmpty()) {
            throw new IllegalArgumentException("Land not found with id: " + sowingDTO.getLandId());
        }
        Sowing sowing = convertToEntity(sowingDTO, land.get());
        Sowing savedSowing = sowingRepository.save(sowing);
        return convertToDTO(savedSowing);
    }

    private SowingDTO convertToDTO(Sowing sowing) {
        SowingDTO dto = new SowingDTO();
        dto.setId(sowing.getId());
        dto.setLandId(sowing.getLand().getId());
        dto.setPlantingAmount(sowing.getPlantingAmount());
        dto.setSowingDate(sowing.getSowingDate());
        return dto;
    }

    private Sowing convertToEntity(SowingDTO dto, Land land) {
        Sowing sowing = new Sowing();
        sowing.setLand(land);
        sowing.setPlantingAmount(dto.getPlantingAmount());
        sowing.setSowingDate(dto.getSowingDate());
        return sowing;
    }
    public List<SowingDTO> getSowingsByUserId(Long userId) {
        List<Long> landIds = landRepository.findByUserId(userId)
                .stream()
                .map(Land::getId)
                .collect(Collectors.toList());

        return sowingRepository.findByLandIdIn(landIds)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

}
