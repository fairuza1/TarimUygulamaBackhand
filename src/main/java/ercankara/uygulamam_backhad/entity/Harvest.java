package ercankara.uygulamam_backhad.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "harvests")
public class Harvest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "harvest_date", nullable = false)
    private LocalDate harvestDate;

    @ManyToOne
    @JoinColumn(name = "sowing_id", nullable = false)
    @JsonBackReference // Bu alan Json serileştirmede döngüyü önler
    private Sowing sowing;


    @OneToMany(mappedBy = "harvest", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rating> ratings;

}
