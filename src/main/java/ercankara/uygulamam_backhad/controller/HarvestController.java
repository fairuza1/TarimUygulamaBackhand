package ercankara.uygulamam_backhad.controller;

import ercankara.uygulamam_backhad.dto.HarvestDTO;
import ercankara.uygulamam_backhad.service.HarvestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/harvests")
public class HarvestController {
    private final HarvestService harvestService;

    @Autowired
    public HarvestController(HarvestService harvestService) {
        this.harvestService = harvestService;

    }

    @GetMapping
    public ResponseEntity<List<HarvestDTO>> getAllHarvests() {
        List<HarvestDTO> harvests = harvestService.getAllHarvests();
        return new ResponseEntity<>(harvests, HttpStatus.OK);

    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<HarvestDTO>> getHarvestsByUser(@PathVariable Long userId) {
        List<HarvestDTO> harvests = harvestService.getHarvestsByUserId(userId);
        if (harvests.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(harvests);
    }

    @PostMapping
    public ResponseEntity<HarvestDTO> createHarvest(@RequestBody HarvestDTO harvestDTO) {
        HarvestDTO createdHarvest = harvestService.createHarvest(harvestDTO);
        return new ResponseEntity<>(createdHarvest, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHarvest(@PathVariable Long id) {
        HarvestDTO harvest = harvestService.getHarvestById(id);
        if (harvest == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            // Hasat ile ilişkili değerlendirmeleri sil

            harvestService.deleteHarvest(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
