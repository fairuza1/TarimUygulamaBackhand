package ercankara.uygulamam_backhad.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "sowing")
public class Sowing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @ManyToOne
    @JoinColumn(name = "plant_id", nullable = false)
    private Plant plant;

    @ManyToOne
    @JoinColumn(name = "land_id", nullable = false)
    @JsonBackReference
    private Land land;

    @Column(nullable = false)
    private int plantingAmount;

    @Column(nullable = false)
    private LocalDate sowingDate;
    // Kategori bilgisi
    private Long categoryId;      // Kategori ID
    private String categoryName;  // Kategori adı
}
