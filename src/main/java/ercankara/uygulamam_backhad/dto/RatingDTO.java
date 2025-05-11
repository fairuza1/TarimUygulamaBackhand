package ercankara.uygulamam_backhad.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingDTO {
    private Long id;
    private Long harvestId; // İlgili hasat ID'si (gerekli olacak)
    private String comment;
    private Integer harvestStatus;
    private Map<String, Integer> categoryRatings;
    private List<String> tags;
    private Double amount;
    private Double yieldPerSquareMeter;



}
