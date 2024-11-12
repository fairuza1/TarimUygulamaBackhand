package ercankara.uygulamam_backhad.service;


import ercankara.uygulamam_backhad.dto.LandDTO;
import ercankara.uygulamam_backhad.entity.Land;
import ercankara.uygulamam_backhad.entity.User;
import ercankara.uygulamam_backhad.repository.LandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LandService {

    private final LandRepository landRepository;

    @Autowired
    public LandService(LandRepository landRepository) {
        this.landRepository = landRepository;
    }

    public Land saveLand(LandDTO landDTO, User user) {
        Land land = new Land();
        land.setName(landDTO.getName());
        land.setLandSize(landDTO.getLandSize());
        land.setCity(landDTO.getCity());
        land.setDistrict(landDTO.getDistrict());
        land.setVillage(landDTO.getVillage());
        land.setUser(user);

        return landRepository.save(land);
    }

    public List<Land> getAllLands() {
        return landRepository.findAll();
    }
}
