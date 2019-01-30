package pl.ljedrzynski.iparkapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.ljedrzynski.iparkapp.domain.ParkingOccupation;

@Repository
public interface ParkingOccupationRepository extends JpaRepository<ParkingOccupation, Long> {
}
