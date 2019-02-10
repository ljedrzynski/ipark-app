package pl.ljedrzynski.iparkapp.service;

import pl.ljedrzynski.iparkapp.service.dto.ParkingFeeDTO;
import pl.ljedrzynski.iparkapp.service.dto.ParkingOccupationDTO;

public interface ParkingOccupationService {

    ParkingOccupationDTO startOccupation(String regNumber, boolean isVip);

    void stopOccupation(String regNumber);

    ParkingFeeDTO getParkingFee(String regNumber);

    ParkingOccupationDTO getParkingOccupation(String regNumber);
}
