package ercankara.uygulamam_backhad.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SowingDTO {
    private Long id;
    private Long landId;
    private int plantingAmount;
    private LocalDate sowingDate;
    private Long plantId;          // Bitki ID'si eklendi

    // Kategori bilgisi ekleniyor
    private Long categoryId;      // Yeni kategori ID alanı
    private String categoryName;  // Yeni kategori adı alanı

    private String landName;
    private String plantName;

}
