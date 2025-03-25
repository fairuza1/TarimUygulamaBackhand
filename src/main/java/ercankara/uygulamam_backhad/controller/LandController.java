package ercankara.uygulamam_backhad.controller;

import ercankara.uygulamam_backhad.dto.LandDTO;
import ercankara.uygulamam_backhad.entity.Land;
import ercankara.uygulamam_backhad.entity.User;
import ercankara.uygulamam_backhad.service.LandService;
import ercankara.uygulamam_backhad.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/lands")
public class LandController {

    private final LandService landService;
    private final UserService userService;

    @Value("${file.upload-dir}") // application.properties'ten gelen değer
    private String uploadDir;

    public LandController(LandService landService, UserService userService) {
        this.landService = landService;
        this.userService = userService;
    }

    // Arazi ekleme metodu
    @PostMapping
    public ResponseEntity<LandDTO> createLand(
            @RequestParam("name") String name,
            @RequestParam("landSize") double landSize,
            @RequestParam("city") String city,
            @RequestParam("district") String district,
            @RequestParam("village") String village,
            @RequestParam("userId") Long userId,
            @RequestParam(value = "photo", required = false) MultipartFile photo) throws IOException {

        if (userId == null) {
            throw new IllegalArgumentException("User ID boş olamaz.");
        }

        User user = userService.findById(userId)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        LandDTO landDto = new LandDTO();
        landDto.setName(name);
        landDto.setLandSize(landSize);
        landDto.setCity(city);
        landDto.setDistrict(district);
        landDto.setVillage(village);
        landDto.setUserId(user.getId());

        if (photo != null && !photo.isEmpty()) {
            // Dinamik olarak dosya yükleme dizini alınır
            File uploadFolder = new File(uploadDir);
            if (!uploadFolder.exists()) uploadFolder.mkdirs();

            String fileName = System.currentTimeMillis() + "_" + photo.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);
            photo.transferTo(filePath.toFile());

            // Fotoğraf yolunu kaydedelim
            landDto.setPhotoPath(fileName);
        }

        Land land = landService.saveLand(landDto);
        LandDTO savedLandDto = landService.getLandById(land.getId());
        return new ResponseEntity<>(savedLandDto, HttpStatus.CREATED);
    }

    // Kullanıcıya ait arazileri listeleme metodu
    @GetMapping
    public ResponseEntity<List<LandDTO>> getLandsByUser(@RequestParam Long userId) {
        User user = userService.findById(userId)
                .orElseThrow(() -> new RuntimeException("User with ID " + userId + " not found"));

        List<LandDTO> lands = landService.getLandsByUser(user.getId());

        if (lands.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(lands);
    }

    // Arazi silme metodu
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLand(@PathVariable Long id) {
        landService.deleteLand(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/detail/{id}")
    public LandDTO getLandById(@PathVariable Long id) {
        return landService.getLandById(id);
    }

    // Arazi güncelleme metodu
    @PutMapping("/{id}")
    public ResponseEntity<LandDTO> updateLand(@PathVariable Long id, @RequestBody LandDTO updatedLandDto) {
        LandDTO updatedLand = landService.updateLand(id, updatedLandDto);
        return ResponseEntity.ok(updatedLand);
    }

    @GetMapping("/photo/{fileName}")
    public ResponseEntity<byte[]> getPhoto(@PathVariable String fileName) throws IOException {
        Path filePath = Paths.get(uploadDir, fileName);

        if (!Files.exists(filePath)) {
            return ResponseEntity.notFound().build();
        }

        byte[] imageBytes = Files.readAllBytes(filePath);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageBytes);
    }

}
