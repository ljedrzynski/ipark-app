package pl.ljedrzynski.iparkapp.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.ljedrzynski.iparkapp.common.exceptions.BadRequestException;
import pl.ljedrzynski.iparkapp.domain.ParkingOccupation;
import pl.ljedrzynski.iparkapp.repository.ParkingOccupationRepository;
import pl.ljedrzynski.iparkapp.service.ParkingOccupationService;
import pl.ljedrzynski.iparkapp.service.mapper.ParkingFeeMapper;
import pl.ljedrzynski.iparkapp.service.mapper.ParkingOccupationMapper;
import pl.ljedrzynski.iparkapp.service.dto.ParkingFeeDTO;
import pl.ljedrzynski.iparkapp.service.dto.ParkingOccupationDTO;

import java.time.Clock;
import java.time.LocalDateTime;

@Slf4j
@Service
public class ParkingOccupationServiceImpl implements ParkingOccupationService {

    private final ParkingOccupationRepository parkingOccupationRepository;
    private final ParkingOccupationMapper parkingOccupationMapper;
    private final ParkingFeeMapper parkingFeeMapper;
    private final Clock clock;

    @Autowired
    public ParkingOccupationServiceImpl(ParkingOccupationRepository parkingOccupationRepository, ParkingOccupationMapper parkingOccupationMapper, ParkingFeeMapper parkingFeeMapper) {
        this.parkingOccupationRepository = parkingOccupationRepository;
        this.parkingOccupationMapper = parkingOccupationMapper;
        this.parkingFeeMapper = parkingFeeMapper;
        this.clock = Clock.systemDefaultZone();
    }


    public ParkingOccupationServiceImpl(ParkingOccupationRepository parkingOccupationRepository, ParkingOccupationMapper parkingOccupationMapper, ParkingFeeMapper parkingFeeMapper, Clock clock) {
        this.parkingOccupationRepository = parkingOccupationRepository;
        this.parkingOccupationMapper = parkingOccupationMapper;
        this.parkingFeeMapper = parkingFeeMapper;
        this.clock = clock;
    }

    @Transactional
    public ParkingOccupationDTO startOccupation(String regNumber, boolean isVip) {
        log.debug("Request to start occupation for regNumber: {}", regNumber);

        parkingOccupationRepository.findActiveParkingOccupation(regNumber)
                .ifPresent(t -> {
                    throw new BadRequestException("Parking occupation already registered and active");
                });

        var parkingOccupation = new ParkingOccupation();
        parkingOccupation.setRegistrationNumber(regNumber);
        parkingOccupation.setStartDate(LocalDateTime.now(clock));
        parkingOccupation.setIsVip(isVip);

        parkingOccupationRepository.save(parkingOccupation);

        return parkingOccupationMapper.toDTO(parkingOccupation);
    }

    @Transactional
    public void stopOccupation(String regNumber) {
        log.debug("Request to stop occupation for regNumber: {}", regNumber);
        var parkingOccupation = getParkingOccupationOrElseThrowException(regNumber);
        parkingOccupation.setEndDate(LocalDateTime.now(clock));
    }

    public ParkingFeeDTO getParkingFee(String regNumber) {
//        log.debug("Request to get parking fee for regNumber: {}", regNumber);
//        var parkingOccupation = getParkingOccupation(regNumber);
//        var parkingFee = new ParkingFeeDTO();
        return null;
    }

    public ParkingOccupationDTO getParkingOccupation(String regNumber) {
        log.debug("Request to get parking occupation for regNumber: {}", regNumber);
        var parkingOccupation = getParkingOccupationOrElseThrowException(regNumber);
        return parkingOccupationMapper.toDTO(parkingOccupation);
    }

    private ParkingOccupation getParkingOccupationOrElseThrowException(String regNumber) {
        return parkingOccupationRepository.findActiveParkingOccupation(regNumber)
                .orElseThrow(() -> new BadRequestException("Cannot found any active occupation for registrationNumber: {0}", regNumber));
    }
}
