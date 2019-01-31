package pl.ljedrzynski.iparkapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.ljedrzynski.iparkapp.domain.ParkingOccupation;

import java.util.Optional;

@Repository
public interface ParkingOccupationRepository extends JpaRepository<ParkingOccupation, Long> {

    @Query("select p from ParkingOccupation p where p.registrationNumber = :regNum and p.startDate < current_time and p.endDate is null")
    Optional<ParkingOccupation> findActiveParkingOccupation(@Param("regNum") String registrationNumber);
}
