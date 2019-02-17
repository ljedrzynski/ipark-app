package pl.ljedrzynski.iparkapp.service;

import pl.ljedrzynski.iparkapp.service.dto.ParkingOccupationDTO;

import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;

public interface ParkingFeeService {

    MonetaryAmount getParkingFeeAmount(ParkingOccupationDTO parkingOccupationDTO);

    MonetaryAmount getParkingFeeAmount(CurrencyUnit currency, ParkingOccupationDTO parkingOccupationDTO);
}
