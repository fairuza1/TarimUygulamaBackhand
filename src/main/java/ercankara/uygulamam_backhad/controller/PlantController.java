package ercankara.uygulamam_backhad.controller;

import ercankara.uygulamam_backhad.dto.PlantDTO;
import ercankara.uygulamam_backhad.entity.Plant;
import ercankara.uygulamam_backhad.service.PlantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/plants")
public class PlantController {

    @Autowired
    private PlantService plantService;

    @PostMapping
    public Plant createPlant(@RequestBody PlantDTO plantDto) {
        return plantService.savePlant(plantDto);  // Kategori ile birlikte bitkiyi kaydediyoruz
    }

    @GetMapping
    public List<PlantDTO> getAllPlants() {
        return plantService.getAllPlants();
    }

    @GetMapping("/by-category")
    public List<PlantDTO> getPlantsByCategory(@RequestParam Long categoryId) {
        return plantService.getPlantsByCategory(categoryId);
    }

    @GetMapping("/detail/{id}")
    public PlantDTO getPlantById(@PathVariable Long id) {
        return plantService.getPlantById(id);
    }
}
