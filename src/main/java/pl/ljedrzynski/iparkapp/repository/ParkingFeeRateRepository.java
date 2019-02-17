package pl.ljedrzynski.iparkapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.ljedrzynski.iparkapp.domain.ParkingFeeRate;
import springfox.documentation.annotations.Cacheable;


@Repository
public interface ParkingFeeRateRepository extends JpaRepository<ParkingFeeRate, Long> {

    @Cacheable("parkingFeeRates")
    ParkingFeeRate findByCurrencyCodeAndIsVip(String currencyCode, Boolean isVip);

}
