package ercankara.uygulamam_backhad.controller;

import ercankara.uygulamam_backhad.dto.LandDTO;
import ercankara.uygulamam_backhad.entity.Land;
import ercankara.uygulamam_backhad.entity.User;
import ercankara.uygulamam_backhad.service.LandService;
import ercankara.uygulamam_backhad.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lands")
public class LandController {

    private final LandService landService;
    private final UserService userService;

    public LandController(LandService landService, UserService userService) {
        this.landService = landService;
        this.userService = userService;
    }

    // Arazi ekleme metodu
    @PostMapping
    public ResponseEntity<LandDTO> createLand(@RequestBody LandDTO landDto) {
        if (landDto.getUserId() == null) {
            throw new IllegalArgumentException("User ID boş olamaz.");
        }

        // Kullanıcıyı bulmak için userService kullan
        User user = userService.findById(landDto.getUserId())
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        // LandDTO'ya userId'yi set et
        landDto.setUserId(user.getId());

        // Araziyi kaydet
        Land land = landService.saveLand(landDto);

        // Kaydedilen araziyi HTTP 201 ile döndür
        LandDTO savedLandDto = landService.getLandById(land.getId());
        return new ResponseEntity<>(savedLandDto, HttpStatus.CREATED);
    }

    // Kullanıcıya ait arazileri listeleme metodu (sadece userId ile)
    @GetMapping
    public ResponseEntity<List<LandDTO>> getLandsByUser(@RequestParam Long userId) {

        // Kullanıcıyı userId ile bul
        User user = userService.findById(userId)
                .orElseThrow(() -> new RuntimeException("User with ID " + userId + " not found"));

        // Kullanıcıya ait arazileri getirmek
        List<LandDTO> lands = landService.getLandsByUser(user.getId());

        // Eğer kullanıcıya ait arazi yoksa, HTTP 204 No Content döndürüyoruz
        if (lands.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        // Kullanıcıya ait arazileri HTTP 200 ile döndürüyoruz
        return ResponseEntity.ok(lands);
    }
}
