package ercankara.uygulamam_backhad.controller;

import ercankara.uygulamam_backhad.dto.LandDTO;
import ercankara.uygulamam_backhad.entity.Land;
import ercankara.uygulamam_backhad.entity.User;
import ercankara.uygulamam_backhad.service.LandService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/lands")
public class LandController {

    private final LandService landService;

    @Autowired
    public LandController(LandService landService) {
        this.landService = landService;
    }

    @PostMapping
    public ResponseEntity<String> addLand(@RequestBody LandDTO landDTO) {
        // Kullanıcıyı burada doğrulamalısınız.
        // Örnek olarak, hardcoded bir kullanıcı ile devam edelim
        User user = new User();
        user.setId(1); // Bu ID, oturum açmış kullanıcı ID'si olmalıdır.

        Land land = landService.saveLand(landDTO, user);
        return ResponseEntity.ok("Arazi başarıyla kaydedildi!");
    }

    @GetMapping
    public ResponseEntity<List<Land>> getAllLands() {
        List<Land> lands = landService.getAllLands();
        return ResponseEntity.ok(lands);
    }
}
