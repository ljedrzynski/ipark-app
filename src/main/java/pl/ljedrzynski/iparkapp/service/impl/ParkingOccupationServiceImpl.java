package pl.ljedrzynski.iparkapp.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.ljedrzynski.iparkapp.utils.exception.BadRequestException;
import pl.ljedrzynski.iparkapp.repository.ParkingOccupationRepository;
import pl.ljedrzynski.iparkapp.service.ParkingOccupationService;
import pl.ljedrzynski.iparkapp.service.converter.ParkingOccupationMapper;
import pl.ljedrzynski.iparkapp.service.dto.ParkingOccupationDTO;

import java.time.Clock;
import java.time.LocalDateTime;

@Slf4j
@Service
public class ParkingOccupationServiceImpl implements ParkingOccupationService {

    private final ParkingOccupationRepository parkingOccupationRepository;
    private final ParkingOccupationMapper parkingOccupationMapper;
    private final Clock clock;

    @Autowired
    public ParkingOccupationServiceImpl(ParkingOccupationRepository parkingOccupationRepository, ParkingOccupationMapper parkingOccupationMapper) {
        this.parkingOccupationRepository = parkingOccupationRepository;
        this.parkingOccupationMapper = parkingOccupationMapper;
        this.clock = Clock.systemDefaultZone();
    }

    public ParkingOccupationServiceImpl(ParkingOccupationRepository parkingOccupationRepository, ParkingOccupationMapper parkingOccupationMapper, Clock clock) {
        this.parkingOccupationRepository = parkingOccupationRepository;
        this.parkingOccupationMapper = parkingOccupationMapper;
        this.clock = clock;
    }

    @Transactional
    public ParkingOccupationDTO createOccupation(ParkingOccupationDTO parkingOccupationDTO) {
        log.debug("Request to save occupation : {}", parkingOccupationDTO);

        var opt = parkingOccupationRepository.findActiveParkingOccupation(parkingOccupationDTO.getRegistrationNumber());
        if (opt.isPresent()) {
            throw new BadRequestException("Parking occupation already registered and active");
        }

        var parkingOccupation = parkingOccupationMapper.toEntity(parkingOccupationDTO);
        parkingOccupation.setStartDate(LocalDateTime.now(clock));
        parkingOccupationRepository.save(parkingOccupation);

        return parkingOccupationMapper.toDTO(parkingOccupation);
    }


}
