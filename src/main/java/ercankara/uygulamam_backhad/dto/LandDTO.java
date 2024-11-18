package ercankara.uygulamam_backhad.dto;

import ercankara.uygulamam_backhad.entity.Land;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LandDTO {

    private Long id;
    private String name;
    private double landSize;
    private String city;
    private String district;
    private String village;
    private Long userId;

    // Entity'den DTO'ya dönüşüm
    public LandDTO(Land land) {
        this.id = land.getId();
        this.name = land.getName();
        this.landSize = land.getLandSize();
        this.city = land.getCity();
        this.district = land.getDistrict();
        this.village = land.getVillage();
        this.userId = Long.valueOf(land.getUser().getId());
    }
}
