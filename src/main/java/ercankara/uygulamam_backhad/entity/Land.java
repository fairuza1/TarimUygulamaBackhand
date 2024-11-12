package ercankara.uygulamam_backhad.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "land")
public class Land {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable = false, length = 50)
    String name;
    @Column(nullable = false)
    int landSize;
    @Column(nullable = false)
    String city;
    @Column(nullable = false)
    String district;
    private String landType;
    String village;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
