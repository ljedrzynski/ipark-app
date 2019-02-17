package pl.ljedrzynski.iparkapp.service.impl;

import org.javamoney.moneta.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.ljedrzynski.iparkapp.common.math.MathUtils;
import pl.ljedrzynski.iparkapp.common.properties.ParkingFeeProperties;
import pl.ljedrzynski.iparkapp.repository.ParkingFeeRateRepository;
import pl.ljedrzynski.iparkapp.service.ParkingFeeService;
import pl.ljedrzynski.iparkapp.service.dto.ParkingOccupationDTO;

import javax.money.CurrencyUnit;

import javax.money.Monetary;
import javax.money.MonetaryAmount;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class ParkingFeeServiceImpl implements ParkingFeeService {

    private ParkingFeeRateRepository parkingFeeRateRepository;
    private ParkingFeeProperties parkingFeeProperties;
    private Clock clock;

    @Autowired
    public ParkingFeeServiceImpl(ParkingFeeRateRepository parkingFeeRateRepository, ParkingFeeProperties parkingFeeProperties) {
        this.parkingFeeRateRepository = parkingFeeRateRepository;
        this.parkingFeeProperties = parkingFeeProperties;
        this.clock = Clock.systemDefaultZone();
    }

    public ParkingFeeServiceImpl(ParkingFeeRateRepository parkingFeeRateRepository, ParkingFeeProperties parkingFeeProperties, Clock clock) {
        this.parkingFeeRateRepository = parkingFeeRateRepository;
        this.parkingFeeProperties = parkingFeeProperties;
        this.clock = clock;
    }

    @Override
    public MonetaryAmount getParkingFeeAmount(ParkingOccupationDTO parkingOccupationDTO) {
        return getParkingFeeAmount(Monetary.getCurrency(parkingFeeProperties.getDefaultCurrency()), parkingOccupationDTO);
    }

    @Override
    public MonetaryAmount getParkingFeeAmount(CurrencyUnit currencyUnit, ParkingOccupationDTO parkingOccupationDTO) {
        var hours = parkingOccupationDTO.getStartDate().until(Optional.ofNullable(parkingOccupationDTO.getEndDate()).orElse(LocalDateTime.now(clock)), ChronoUnit.HOURS);

        var parkingFeeRate = parkingFeeRateRepository.findByCurrencyCodeAndIsVip(currencyUnit.getCurrencyCode(), parkingOccupationDTO.getIsVip());

        if (hours < 1) {
            return Money.of(parkingFeeRate.getFirstHourFee(), currencyUnit);
        } else if (hours < 2) {
            return Money.of(parkingFeeRate.getFirstHourFee() + parkingFeeRate.getSecondHourFee(), currencyUnit);
        }

        return Money.of(parkingFeeRate.getFirstHourFee() + MathUtils.geometricSequenceSum(parkingFeeRate.getSecondHourFee(), parkingFeeRate.getNextHourFactor(), Math.toIntExact(hours)), currencyUnit);
    }
}

