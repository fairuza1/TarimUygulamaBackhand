package ercankara.uygulamam_backhad.repository;

import ercankara.uygulamam_backhad.entity.Land;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LandRepository extends JpaRepository<Land, Long> {

}
