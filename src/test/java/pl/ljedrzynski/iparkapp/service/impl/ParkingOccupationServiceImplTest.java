package pl.ljedrzynski.iparkapp.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.ljedrzynski.iparkapp.common.exceptions.BadRequestException;
import pl.ljedrzynski.iparkapp.domain.ParkingOccupation;
import pl.ljedrzynski.iparkapp.repository.ParkingOccupationRepository;
import pl.ljedrzynski.iparkapp.service.ParkingFeeService;
import pl.ljedrzynski.iparkapp.service.ParkingOccupationService;
import pl.ljedrzynski.iparkapp.service.dto.ParkingFeeDTO;
import pl.ljedrzynski.iparkapp.service.dto.ParkingOccupationDTO;
import pl.ljedrzynski.iparkapp.service.mapper.ParkingOccupationMapper;

import javax.money.Monetary;
import javax.money.MonetaryAmount;
import java.time.*;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class ParkingOccupationServiceImplTest {


    private static final String DEF_REG_NUMBER = "PO50012";
    private static final LocalDateTime MOCK_LOCAL_DATE_NOW = LocalDateTime.of(2019, 1, 29, 12, 30, 0);
    private static final LocalDateTime DEF_PARKING_START_DATE = LocalDateTime.of(2019, 1, 29, 12, 0, 0);
    private static final LocalDateTime DEF_PARKING_END_DATE = LocalDateTime.of(2019, 1, 29, 13, 0, 0);
    private static final String DEF_FEE_CURRENCY_CODE = "PLN";
    private static final String DEF_ZONE_ID = "CET";

    @Mock
    private ParkingFeeService parkingFeeService;

    @Mock
    private ParkingOccupationRepository parkingOccupationRepository;

    private ParkingOccupationService parkingOccupationService;
    private int DEF_FEE_AMOUNT = 3;

    @Before
    public void setUp() {
        var clock = Clock.fixed(MOCK_LOCAL_DATE_NOW.atZone(ZoneId.of(DEF_ZONE_ID)).toInstant(), ZoneId.of(DEF_ZONE_ID));
        parkingOccupationService = new ParkingOccupationServiceImpl(parkingOccupationRepository, ParkingOccupationMapper.INSTANCE, parkingFeeService, clock);
    }

    @Test
    public void startOccupation_shouldReturnCreatedOccupation() {
        var result = parkingOccupationService.startOccupation(DEF_REG_NUMBER, false);
        assertThat(result)
                .isEqualTo(ParkingOccupationDTO.builder()
                        .registrationNumber(DEF_REG_NUMBER)
                        .startDate(MOCK_LOCAL_DATE_NOW)
                        .isVip(false)
                        .build());
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
    public void stopOccupation_shouldReturnStoppedOccupation() {
        var parkingOccupation = ParkingOccupation.builder()
                .registrationNumber(DEF_REG_NUMBER)
                .startDate(DEF_PARKING_START_DATE)
                .isVip(true)
                .build();

        when(parkingOccupationRepository.findActiveParkingOccupation(anyString()))
                .thenReturn(Optional.ofNullable(parkingOccupation));
        when(parkingFeeService.getParkingFeeAmount(any()))
                .thenReturn(getDefaultMonetaryAmount());

        var parkingOccupationDTO = parkingOccupationService.stopOccupation(DEF_REG_NUMBER);

        assertThat(parkingOccupationDTO)
                .isEqualTo(
                        ParkingOccupationDTO.builder()
                                .registrationNumber(DEF_REG_NUMBER)
                                .startDate(DEF_PARKING_START_DATE)
                                .isVip(true)
                                .parkingFeeDTO(ParkingFeeDTO.builder()
                                        .feeAmount(DEF_FEE_AMOUNT)
                                        .feeCurrency(DEF_FEE_CURRENCY_CODE).build())
                                .endDate(MOCK_LOCAL_DATE_NOW)
                                .build());
    }

    @Test
    public void getParkingOccupation_shouldReturnParkingOccupation() {
        var parkingOccupation = ParkingOccupation.builder()
                .registrationNumber(DEF_REG_NUMBER)
                .startDate(DEF_PARKING_START_DATE)
                .endDate(DEF_PARKING_END_DATE)
                .isVip(true)
                .build();

        when(parkingOccupationRepository.findActiveParkingOccupation(anyString()))
                .thenReturn(Optional.of(parkingOccupation));

        var parkingOccupationDTO = parkingOccupationService.getParkingOccupation(DEF_REG_NUMBER);

        assertThat(parkingOccupationDTO)
                .isEqualTo(ParkingOccupationDTO.builder()
                        .registrationNumber(DEF_REG_NUMBER)
                        .startDate(DEF_PARKING_START_DATE)
                        .endDate(DEF_PARKING_END_DATE)
                        .isVip(true)
                        .build());
    }

    @Test(expected = BadRequestException.class)
    public void getParkingOccupation_shouldThrowException_whenParkingOccupationNotFound() {
        when(parkingOccupationRepository.findActiveParkingOccupation(anyString()))
                .thenReturn(Optional.empty());
        parkingOccupationService.getParkingOccupation(DEF_REG_NUMBER);
    }

    private MonetaryAmount getDefaultMonetaryAmount() {
        return Monetary.getDefaultAmountFactory()
                .setCurrency(DEF_FEE_CURRENCY_CODE)
                .setNumber(DEF_FEE_AMOUNT)
                .create();
    }

}