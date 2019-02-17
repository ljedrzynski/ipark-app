package pl.ljedrzynski.iparkapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.ljedrzynski.iparkapp.domain.ParkingFeeRate;

@Repository
public interface ParkingFeeRepository extends JpaRepository<ParkingFeeRate, Long> {

}
