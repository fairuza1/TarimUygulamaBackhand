package ercankara.uygulamam_backhad.service;

import ercankara.uygulamam_backhad.dto.LandDTO;
import ercankara.uygulamam_backhad.entity.Land;
import ercankara.uygulamam_backhad.entity.User;
import ercankara.uygulamam_backhad.repository.LandRepository;
import ercankara.uygulamam_backhad.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LandService {

    private final LandRepository landRepository;
    private final UserRepository userRepository;

    public LandService(LandRepository landRepository, UserRepository userRepository) {
        this.landRepository = landRepository;
        this.userRepository = userRepository;
    }

    // Araziyi kaydetme
    public Land saveLand(LandDTO landDto) {
        Land land = new Land();
        land.setName(landDto.getName());
        land.setLandSize(landDto.getLandSize());
        land.setCity(landDto.getCity());
        land.setDistrict(landDto.getDistrict());
        land.setVillage(landDto.getVillage());
        land.setRemainingSize(landDto.getLandSize());

        // Kullanıcıyı bul ve ilişkilendir
        User user = userRepository.findById(landDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        land.setUser(user);

        // Fotoğraf yolu ekleyelim
        if (landDto.getPhotoPath() != null) {
            land.setPhotoPath(landDto.getPhotoPath());
        }

        // Kaydet ve döndür
        return landRepository.save(land);
    }

    // Kullanıcıya ait arazileri listele
    public List<LandDTO> getLandsByUser(Long userId) {
        List<Land> lands = landRepository.findByUserId(userId);
        return lands.stream().map(LandDTO::new).toList();
    }

    // ID'ye göre arazi getirme
    public LandDTO getLandById(Long id) {
        Land land = landRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Land not found"));
        return new LandDTO(land);
    }

    // Arazi silme
    public void deleteLand(Long id) {
        Land land = landRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Land with ID " + id + " not found"));
        landRepository.delete(land);
    }

    // Arazi güncelleme
    public LandDTO updateLand(Long id, LandDTO updatedLandDto) {
        Land existingLand = landRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Land with ID " + id + " not found"));

        double usedSize = existingLand.getLandSize() - existingLand.getRemainingSize();

        // Yeni boyut, kullanılan alandan küçükse hata ver
        if (updatedLandDto.getLandSize() < usedSize) {
            throw new IllegalArgumentException("Yeni arazi boyutu, yapılan ekimlerden küçük olamaz.");
        }

        double newRemainingSize = updatedLandDto.getLandSize() - usedSize;

        existingLand.setName(updatedLandDto.getName());
        existingLand.setLandSize(updatedLandDto.getLandSize());
        existingLand.setCity(updatedLandDto.getCity());
        existingLand.setDistrict(updatedLandDto.getDistrict());
        existingLand.setVillage(updatedLandDto.getVillage());
        existingLand.setRemainingSize(newRemainingSize); // 🔧 Fix burada

        if (updatedLandDto.getPhotoPath() != null) {
            existingLand.setPhotoPath(updatedLandDto.getPhotoPath());
        }

        Land updatedLand = landRepository.save(existingLand);
        return new LandDTO(updatedLand);
    }
    public long getTotalLandCount() {
        return landRepository.count();
    }
}
