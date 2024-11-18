package ercankara.uygulamam_backhad.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Land {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private double landSize;
    private String city;
    private String district;
    private String village;

    // Kullanıcı ile ilişki
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
