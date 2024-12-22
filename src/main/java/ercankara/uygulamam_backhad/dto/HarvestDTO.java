package ercankara.uygulamam_backhad.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class HarvestDTO {
    private Long id;
    private LocalDate harvestDate;
    private Long sowingId;

    private String plantName;      // Bitki adı
    private String categoryName;   // Kategori adı
    private String landName;       // Arazi adı
    private int plantingAmount;    // Ekim miktarı
}
