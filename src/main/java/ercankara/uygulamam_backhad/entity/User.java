package ercankara.uygulamam_backhad.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference; // Import et
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    private String password;

    // Kullanıcıya ait araziler
    @OneToMany(mappedBy = "user")
    @JsonManagedReference // Bu, 'lands' listesinin serileştirilmesini sağlar
    private List<Land> lands;
}
