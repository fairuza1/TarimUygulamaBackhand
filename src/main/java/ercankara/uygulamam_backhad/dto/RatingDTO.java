package ercankara.uygulamam_backhad.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingDTO {
    private Long id;
    private Long harvestId; // İlgili hasat ID'si (gerekli olacak)

}
