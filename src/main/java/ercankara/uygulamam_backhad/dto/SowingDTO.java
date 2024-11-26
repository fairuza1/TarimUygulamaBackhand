package ercankara.uygulamam_backhad.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SowingDTO {
    private Long id;
    private Long landId;
    private int plantingAmount;
    private LocalDate sowingDate;
}
