package ercankara.uygulamam_backhad.controller;

import ercankara.uygulamam_backhad.dto.LandDTO;
import ercankara.uygulamam_backhad.entity.Land;
import ercankara.uygulamam_backhad.entity.User;
import ercankara.uygulamam_backhad.service.LandService;
import ercankara.uygulamam_backhad.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
        LandDTO savedLandDto = landService.getLandById(land.getId());

        // Kaydedilen araziyi HTTP 201 ile döndür
        return new ResponseEntity<>(savedLandDto, HttpStatus.CREATED);
    }

    // Kullanıcıya ait arazileri listeleme metodu
    @GetMapping
    public List<LandDTO> getLandsByUser() {
        // Oturum açan kullanıcının kimliğini almak
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Kullanıcıyı bulmak için UserService kullan
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Kullanıcıya ait arazileri getirmek
        return landService.getLandsByUser(user.getId());
    }
}
