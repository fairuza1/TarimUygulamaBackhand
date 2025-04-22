package ercankara.uygulamam_backhad.controller;

import ercankara.uygulamam_backhad.dto.SowingDTO;
import ercankara.uygulamam_backhad.service.SowingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sowings")
@RequiredArgsConstructor
public class SowingController {

    private final SowingService sowingService;

    @GetMapping("/land/{landId}")
    public ResponseEntity<List<SowingDTO>> getSowingsByLand(@PathVariable Long landId) {
        List<SowingDTO> sowings = sowingService.getSowingsByLandId(landId);
        if (sowings.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(sowings);
    }

    @PostMapping
    public ResponseEntity<SowingDTO> createSowing(@RequestBody SowingDTO sowingDTO) {
        try {
            SowingDTO createdSowing = sowingService.createSowing(sowingDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdSowing);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SowingDTO>> getSowingsByUser(@PathVariable Long userId) {
        List<SowingDTO> sowings = sowingService.getSowingsByUserId(userId);
        if (sowings.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(sowings);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSowing(@PathVariable Long id) {
        try {
            sowingService.deleteSowing(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace(); // Burayı konsola yazdır, hatayı yakalayalım
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}
