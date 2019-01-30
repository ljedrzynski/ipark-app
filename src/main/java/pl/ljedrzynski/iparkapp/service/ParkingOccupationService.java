package pl.ljedrzynski.iparkapp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.ljedrzynski.iparkapp.repository.ParkingOccupationRepository;
import pl.ljedrzynski.iparkapp.service.converter.ParkingMapper;
import pl.ljedrzynski.iparkapp.service.dto.ParkingOccupationDTO;

@Slf4j
@Service
public class ParkingOccupationService {

    private ParkingOccupationRepository parkingOccupationRepository;
    private ParkingMapper parkingMapper;

    public ParkingOccupationService(ParkingOccupationRepository parkingOccupationRepository, ParkingMapper parkingMapper) {
        this.parkingOccupationRepository = parkingOccupationRepository;
        this.parkingMapper = parkingMapper;
    }

    @Transactional
    public ParkingOccupationDTO registerOccupation(ParkingOccupationDTO parkingOccupationDTO) {
        return null;
    }


}
