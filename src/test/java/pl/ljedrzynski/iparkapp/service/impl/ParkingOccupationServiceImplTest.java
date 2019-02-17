package pl.ljedrzynski.iparkapp.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.ljedrzynski.iparkapp.common.exceptions.BadRequestException;
import pl.ljedrzynski.iparkapp.domain.ParkingOccupation;
import pl.ljedrzynski.iparkapp.repository.ParkingOccupationRepository;
import pl.ljedrzynski.iparkapp.service.impl.ParkingOccupationServiceImpl;
import pl.ljedrzynski.iparkapp.service.mapper.ParkingFeeMapper;
import pl.ljedrzynski.iparkapp.service.mapper.ParkingOccupationMapper;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static pl.ljedrzynski.iparkapp.utils.TestUtils.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class ParkingOccupationServiceTest {


    private static final String DEF_REG_NUMBER = "PO50012";

    @Mock
    private ParkingOccupationRepository parkingOccupationRepository;
    private ParkingOccupationService parkingOccupationService;

    @Before
    public void setUp() {
        Clock clock = Clock.fixed(Instant.parse("2019-01-29T12:00:00.00Z"), ZoneId.of("GMT"));
        parkingOccupationService = new ParkingOccupationServiceImpl(parkingOccupationRepository, ParkingOccupationMapper.INSTANCE, clock);
    }

    @Test
    public void startOccupation_shouldReturnCreatedOccupation() {
        var result = parkingOccupationService.startOccupation(DEF_REG_NUMBER, false);
        assertThat(result).isNotNull()
                .hasFieldOrPropertyWithValue(REGISTRATION_NUMBER, DEF_REG_NUMBER)
                .hasFieldOrPropertyWithValue(IS_VIP, false)
                .hasFieldOrPropertyWithValue(START_DATE, LocalDateTime.of(2019, 1, 29, 12, 0, 0, 0));
    }

    @Test(expected = BadRequestException.class)
    public void startOccupation_shouldThrowException_whenOccupationAlreadyStarted() {
        when(parkingOccupationRepository.findActiveParkingOccupation(anyString()))
                .thenReturn(Optional.of(new ParkingOccupation()));
        parkingOccupationService.startOccupation(DEF_REG_NUMBER, false);
    }

    @Test(expected = BadRequestException.class)
    public void stopOccupation_shouldThrowException_whenActiveOccupationNotFound() {
        when(parkingOccupationRepository.findActiveParkingOccupation(anyString()))
                .thenReturn(Optional.empty());
        parkingOccupationService.stopOccupation(DEF_REG_NUMBER);
    }

    @Test
    public void stopOccupation_shouldReturnParkingFee() {
        when(parkingOccupationRepository.findActiveParkingOccupation(anyString()))
                .thenReturn(Optional.of(new ParkingOccupation()));
        parkingOccupationService.stopOccupation(DEF_REG_NUMBER);
    }

    @Test
    public void getParkingOccupation_shouldReturnParkingOccupation() {
        var parkingOccupation = ParkingOccupation.builder()
                .registrationNumber(DEF_REG_NUMBER)
                .startDate(LocalDateTime.now().minusHours(1))
                .endDate(LocalDateTime.now().minusMinutes(5))
                .isVip(true)
                .build();

        when(parkingOccupationRepository.findActiveParkingOccupation(anyString()))
                .thenReturn(Optional.of(parkingOccupation));

        var parkingOccupationDTO = parkingOccupationService.getParkingOccupation(DEF_REG_NUMBER);
        assertThat(parkingOccupationDTO).isNotNull()
                .hasFieldOrPropertyWithValue(REGISTRATION_NUMBER, parkingOccupation.getRegistrationNumber())
                .hasFieldOrPropertyWithValue(START_DATE, parkingOccupation.getStartDate())
                .hasFieldOrPropertyWithValue(END_DATE, parkingOccupation.getEndDate())
                .hasFieldOrPropertyWithValue(IS_VIP, parkingOccupation.getIsVip());
    }

    @Test(expected = BadRequestException.class)
    public void getParkingOccupation_shouldThrowException_whenParkingOccupationNotFound() {
        when(parkingOccupationRepository.findActiveParkingOccupation(anyString()))
                .thenReturn(Optional.empty());
        parkingOccupationService.getParkingOccupation(DEF_REG_NUMBER);
    }

    @Test(expected = BadRequestException.class)
    public void getParkingFee_shouldThrowException_whenParkingOccupationIsNull() {
//        parkingOccupationService.getParkingFee()
    }
}