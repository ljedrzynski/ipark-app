package pl.ljedrzynski.iparkapp.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.ljedrzynski.iparkapp.common.properties.ParkingFeeProperties;
import pl.ljedrzynski.iparkapp.domain.ParkingFeeRate;
import pl.ljedrzynski.iparkapp.repository.ParkingFeeRateRepository;
import pl.ljedrzynski.iparkapp.service.ParkingFeeService;
import pl.ljedrzynski.iparkapp.service.dto.ParkingOccupationDTO;

import javax.money.Monetary;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class ParkingFeeServiceImplTest {

    private static final String DEF_CURRENCY_CODE = "PLN";
    private static final String DEF_REG_NUMBER = "WH200123";
    private static final LocalDateTime MOCK_LOCAL_DATE_NOW = LocalDateTime.of(2019, 2, 10, 13, 30, 0);
    private static final LocalDateTime DEF_PARKING_START_DATE = LocalDateTime.of(2019, 2, 10, 12, 0, 0);
    private static final String DEF_ZONE_ID = "CET";

    @Mock
    private ParkingFeeProperties parkingFeeProperties;

    @Mock
    private ParkingFeeRateRepository parkingFeeRateRepository;

    @Mock
    private ParkingFeeService parkingFeeService;

    @Before
    public void setUp() {
        var clock = Clock.fixed(MOCK_LOCAL_DATE_NOW.atZone(ZoneId.of(DEF_ZONE_ID)).toInstant(), ZoneId.of(DEF_ZONE_ID));
        parkingFeeService = new ParkingFeeServiceImpl(parkingFeeRateRepository, parkingFeeProperties, clock);
        when(parkingFeeProperties.getDefaultCurrency())
                .thenReturn(DEF_CURRENCY_CODE);
    }

    @Test
    public void getParkingFeeAmount_shouldReturnMonetaryAmountOf1_whenDriverIsRegularAndOccupationLastedLessThanAnHour() {
        var parkingFeeRate = getRegularParkingFee();

        when(parkingFeeRateRepository.findByCurrencyCodeAndIsVip(anyString(), anyBoolean()))
                .thenReturn(parkingFeeRate);

        var parkingOccupationDTO = ParkingOccupationDTO.builder().registrationNumber(DEF_REG_NUMBER)
                .isVip(false)
                .startDate(DEF_PARKING_START_DATE)
                .endDate(DEF_PARKING_START_DATE.plusMinutes(59))
                .build();

        var parkingFee = parkingFeeService.getParkingFeeAmount(parkingOccupationDTO);

        assertThat(parkingFee)
                .isEqualTo(Monetary.getDefaultAmountFactory()
                        .setCurrency(DEF_CURRENCY_CODE)
                        .setNumber(1)
                        .create());
    }

    @Test
    public void getParkingFeeAmount_shouldReturnMonetaryAmountOf3_whenDriverIsRegularAndOccupationLastedBetween1And2Hours() {
        var parkingFeeRate = getRegularParkingFee();

        when(parkingFeeRateRepository.findByCurrencyCodeAndIsVip(anyString(), anyBoolean()))
                .thenReturn(parkingFeeRate);

        var parkingOccupationDTO = ParkingOccupationDTO.builder().registrationNumber(DEF_REG_NUMBER)
                .isVip(false)
                .startDate(DEF_PARKING_START_DATE)
                .endDate(DEF_PARKING_START_DATE.plusMinutes(119))
                .build();

        var parkingFee = parkingFeeService.getParkingFeeAmount(parkingOccupationDTO);

        assertThat(parkingFee)
                .isEqualTo(Monetary.getDefaultAmountFactory()
                        .setCurrency(DEF_CURRENCY_CODE)
                        .setNumber(3)
                        .create());
    }

    @Test
    public void getParkingFeeAmount_shouldReturnMonetaryAmountOf7_whenDriverIsRegularAndOccupationLastedBetween2And3Hours() {
        var parkingFeeRate = getRegularParkingFee();

        when(parkingFeeRateRepository.findByCurrencyCodeAndIsVip(anyString(), anyBoolean()))
                .thenReturn(parkingFeeRate);

        var parkingOccupationDTO = ParkingOccupationDTO.builder().registrationNumber(DEF_REG_NUMBER)
                .isVip(false)
                .startDate(DEF_PARKING_START_DATE)
                .endDate(DEF_PARKING_START_DATE.plusMinutes(179))
                .build();

        var parkingFee = parkingFeeService.getParkingFeeAmount(parkingOccupationDTO);

        assertThat(parkingFee)
                .isEqualTo(Monetary.getDefaultAmountFactory()
                        .setCurrency(DEF_CURRENCY_CODE)
                        .setNumber(7)
                        .create());
    }

    @Test
    public void getParkingFeeAmount_shouldReturnMonetaryAmountOf31_whenDriverIsRegularAndOccupationLastedBetween4And5Hours() {
        var parkingFeeRate = getRegularParkingFee();

        when(parkingFeeRateRepository.findByCurrencyCodeAndIsVip(anyString(), anyBoolean()))
                .thenReturn(parkingFeeRate);

        var parkingOccupationDTO = ParkingOccupationDTO.builder().registrationNumber(DEF_REG_NUMBER)
                .isVip(false)
                .startDate(DEF_PARKING_START_DATE)
                .endDate(DEF_PARKING_START_DATE.plusMinutes(250))
                .build();

        var parkingFee = parkingFeeService.getParkingFeeAmount(parkingOccupationDTO);

        assertThat(parkingFee)
                .isEqualTo(Monetary.getDefaultAmountFactory()
                        .setCurrency(DEF_CURRENCY_CODE)
                        .setNumber(31)
                        .create());
    }

    @Test
    public void getParkingFeeAmount_shouldReturnMonetaryAmountOf0_whenDriverIsVipAndOccupationLastedLessThanAnHour() {
        var parkingFeeRate = getVipParkingFee();

        when(parkingFeeRateRepository.findByCurrencyCodeAndIsVip(anyString(), anyBoolean()))
                .thenReturn(parkingFeeRate);

        var parkingOccupationDTO = ParkingOccupationDTO.builder().registrationNumber(DEF_REG_NUMBER)
                .isVip(true)
                .startDate(DEF_PARKING_START_DATE)
                .endDate(DEF_PARKING_START_DATE.plusMinutes(59))
                .build();

        var parkingFee = parkingFeeService.getParkingFeeAmount(parkingOccupationDTO);

        assertThat(parkingFee)
                .isEqualTo(Monetary.getDefaultAmountFactory()
                        .setCurrency(DEF_CURRENCY_CODE)
                        .setNumber(0)
                        .create());
    }

    @Test
    public void getParkingFeeAmount_shouldReturnMonetaryAmountOf2_whenDriverIsVipAndOccupationLastedBetween1And2Hours() {
        var parkingFeeRate = getVipParkingFee();

        when(parkingFeeRateRepository.findByCurrencyCodeAndIsVip(anyString(), anyBoolean()))
                .thenReturn(parkingFeeRate);

        var parkingOccupationDTO = ParkingOccupationDTO.builder().registrationNumber(DEF_REG_NUMBER)
                .isVip(true)
                .startDate(DEF_PARKING_START_DATE)
                .endDate(DEF_PARKING_START_DATE.plusMinutes(119))
                .build();

        var parkingFee = parkingFeeService.getParkingFeeAmount(parkingOccupationDTO);

        assertThat(parkingFee)
                .isEqualTo(Monetary.getDefaultAmountFactory()
                        .setCurrency(DEF_CURRENCY_CODE)
                        .setNumber(2)
                        .create());
    }

    @Test
    public void getParkingFeeAmount_shouldReturnMonetaryAmountOf5_whenDriverIsVipAndOccupationLastedBetween2And3Hours() {
        var parkingFeeRate = getVipParkingFee();

        when(parkingFeeRateRepository.findByCurrencyCodeAndIsVip(anyString(), anyBoolean()))
                .thenReturn(parkingFeeRate);

        var parkingOccupationDTO = ParkingOccupationDTO.builder().registrationNumber(DEF_REG_NUMBER)
                .isVip(true)
                .startDate(DEF_PARKING_START_DATE)
                .endDate(DEF_PARKING_START_DATE.plusMinutes(179))
                .build();

        var parkingFee = parkingFeeService.getParkingFeeAmount(parkingOccupationDTO);

        assertThat(parkingFee)
                .isEqualTo(Monetary.getDefaultAmountFactory()
                        .setCurrency(DEF_CURRENCY_CODE)
                        .setNumber(5)
                        .create());
    }

    @Test
    public void getParkingFeeAmount_shouldReturnMonetaryAmountOf16_25_whenDriverIsVipAndOccupationLastedBetween4And5Hours() {
        var parkingFeeRate = getVipParkingFee();

        when(parkingFeeRateRepository.findByCurrencyCodeAndIsVip(anyString(), anyBoolean()))
                .thenReturn(parkingFeeRate);

        var parkingOccupationDTO = ParkingOccupationDTO.builder().registrationNumber(DEF_REG_NUMBER)
                .isVip(true)
                .startDate(DEF_PARKING_START_DATE)
                .endDate(DEF_PARKING_START_DATE.plusMinutes(250))
                .build();

        var parkingFee = parkingFeeService.getParkingFeeAmount(parkingOccupationDTO);

        assertThat(parkingFee)
                .isEqualTo(Monetary.getDefaultAmountFactory()
                        .setCurrency(DEF_CURRENCY_CODE)
                        .setNumber(16.25)
                        .create());
    }

    @Test
    public void getParkingFeeAmount_shouldReturnMonetaryAmountOf14_whenDriverIsVipAndOccupationLastedBetween5And6Hours() {
        var parkingFeeRate = getVipParkingFee();

        when(parkingFeeRateRepository.findByCurrencyCodeAndIsVip(anyString(), anyBoolean()))
                .thenReturn(parkingFeeRate);

        var parkingOccupationDTO = ParkingOccupationDTO.builder().registrationNumber(DEF_REG_NUMBER)
                .isVip(true)
                .startDate(DEF_PARKING_START_DATE)
                .endDate(DEF_PARKING_START_DATE.plusMinutes(330))
                .build();

        var parkingFee = parkingFeeService.getParkingFeeAmount(parkingOccupationDTO);

        assertThat(parkingFee)
                .isEqualTo(Monetary.getDefaultAmountFactory()
                        .setCurrency(DEF_CURRENCY_CODE)
                        .setNumber(26.38)
                        .create());
    }

    @Test
    public void getParkingFeeAmount_shouldReturnMonetaryAmountOf3_whenOccupationLasted2HoursAndIsStillOngoing() {
        var parkingFeeRate = getRegularParkingFee();

        when(parkingFeeRateRepository.findByCurrencyCodeAndIsVip(anyString(), anyBoolean()))
                .thenReturn(parkingFeeRate);

        var parkingOccupationDTO = ParkingOccupationDTO.builder().registrationNumber(DEF_REG_NUMBER)
                .isVip(false)
                .startDate(DEF_PARKING_START_DATE)
                .build();

        var parkingFee = parkingFeeService.getParkingFeeAmount(parkingOccupationDTO);

        assertThat(parkingFee)
                .isEqualTo(Monetary.getDefaultAmountFactory()
                        .setCurrency(DEF_CURRENCY_CODE)
                        .setNumber(3)
                        .create());
    }

    private ParkingFeeRate getRegularParkingFee() {
        return ParkingFeeRate.builder()
                .currencyCode(DEF_CURRENCY_CODE)
                .firstHourFee(1.00)
                .secondHourFee(2.00)
                .nextHourFactor(2d)
                .isVip(false)
                .build();
    }

    private ParkingFeeRate getVipParkingFee() {
        return ParkingFeeRate.builder()
                .currencyCode(DEF_CURRENCY_CODE)
                .firstHourFee(0.00)
                .secondHourFee(2.00)
                .nextHourFactor(1.5)
                .isVip(true)
                .build();
    }

}