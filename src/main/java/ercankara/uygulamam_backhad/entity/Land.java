package ercankara.uygulamam_backhad.entity;

import com.fasterxml.jackson.annotation.JsonBackReference; // Import et
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
    private double remainingSize;
    // Fotoğraf yolu ekleniyor
    private String photoPath;


    // Kullanıcı ile ilişki
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference // Bu, 'user' alanının serileştirilmesini engeller
    private User user;
}
