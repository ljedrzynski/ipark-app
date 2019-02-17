package pl.ljedrzynski.iparkapp.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import pl.ljedrzynski.iparkapp.IparkAppApplication;
import pl.ljedrzynski.iparkapp.domain.ParkingOccupation;
import pl.ljedrzynski.iparkapp.repository.ParkingOccupationRepository;
import pl.ljedrzynski.iparkapp.web.rest.request.StartOccupationRequest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.ljedrzynski.iparkapp.config.JacksonConfig.FORMATTER;
import static pl.ljedrzynski.iparkapp.utils.TestUtils.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@WebAppConfiguration
@SpringBootTest(classes = IparkAppApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ParkingOccupationResourceIntTest {

    private static final String API_PARKING_OCCUPATIONS_URI = "/api/parking-occupations";
    private static final String API_PARKING_OCCUPATIONS_START_URI = API_PARKING_OCCUPATIONS_URI + "/start";
    private static final String API_PARKING_OCCUPATIONS_STOP_URI = API_PARKING_OCCUPATIONS_URI + "/stop";
    private static final String DEFAULT_REG_NUMBER = "WWL50012";
    public static final String DEF_CURRENCY_CODE = "PLN";
    public static final int DEF_FEE_AMOUNT = 1;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private ParkingOccupationRepository parkingOccupationRepository;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void post_parkingOccupationStartURI_shouldStartOccupation_whenMockMVC() throws Exception {
        var sizeBeforeTest = parkingOccupationRepository.findAll().size();
        var startOccupationRequest = StartOccupationRequest.builder()
                .registrationNumber(DEFAULT_REG_NUMBER)
                .isVip(false)
                .build();

        mockMvc.perform(post(API_PARKING_OCCUPATIONS_START_URI)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(startOccupationRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        var occupations = parkingOccupationRepository.findAll();
        assertThat(occupations).hasSize(sizeBeforeTest + 1);

        var parkingOccupation = occupations.get(occupations.size() - 1);
        assertThat(parkingOccupation.getRegistrationNumber()).isEqualTo(DEFAULT_REG_NUMBER);
        assertThat(parkingOccupation.getIsVip()).isEqualTo(false);
        assertThat(parkingOccupation.getStartDate()).isNotNull();
    }

    @Test
    public void post_parkingOccupationStartURI_shouldReturnClientError_whenRegistrationNumberIsNull() throws Exception {
        var startOccupationRequest = new StartOccupationRequest();

        mockMvc.perform(post(API_PARKING_OCCUPATIONS_START_URI)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(startOccupationRequest)))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message").value("registrationNumber: must not be null"))
                .andReturn();
    }

    @Test
    public void post_parkingOccupationStartURI_shouldReturnClientError_whenRegistrationNumberIsInvalid() throws Exception {
        var startOccupationRequest = StartOccupationRequest.builder()
                .registrationNumber("0000")
                .build();

        mockMvc.perform(post(API_PARKING_OCCUPATIONS_START_URI)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(startOccupationRequest)))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message").value("registrationNumber: must match \"^[A-Z]{1,3}([A-Z0-9]){1,5}$\""))
                .andReturn();
    }

    @Test
    public void post_parkingOccupationStartURI_shouldReturnClientError_whenOccupationIsAlreadyActive() throws Exception {
        var parkingOccupation = ParkingOccupation.builder()
                .registrationNumber(DEFAULT_REG_NUMBER)
                .startDate(LocalDateTime.now())
                .build();

        parkingOccupationRepository.save(parkingOccupation);

        var startOccupationRequest = StartOccupationRequest.builder()
                .registrationNumber(parkingOccupation.getRegistrationNumber())
                .isVip(parkingOccupation.getIsVip())
                .build();

        mockMvc.perform(post(API_PARKING_OCCUPATIONS_START_URI)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(startOccupationRequest)))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message").value("Parking occupation already registered and active"))
                .andReturn();
    }

    @Test
    public void post_parkingOccupationStopURI_shouldStopOccupation_whenMockMVC() throws Exception {
        var startDate = LocalDateTime.now();
        var mockParkingOccupation = ParkingOccupation.builder()
                .registrationNumber(DEFAULT_REG_NUMBER)
                .startDate(startDate)
                .isVip(false)
                .build();

        parkingOccupationRepository.save(mockParkingOccupation);

        var sizeBeforeTest = parkingOccupationRepository.findAll().size();

        mockMvc.perform(post(API_PARKING_OCCUPATIONS_STOP_URI)
                .contentType(MediaType.TEXT_PLAIN)
                .content(DEFAULT_REG_NUMBER))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.registrationNumber").value(DEFAULT_REG_NUMBER))
                .andExpect(jsonPath("$.startDate").value(FORMATTER.format(startDate)))
                .andExpect(jsonPath("$.endDate").exists())
                .andExpect(jsonPath("$.parkingFee.feeAmount").value(DEF_FEE_AMOUNT))
                .andExpect(jsonPath("$.parkingFee.feeCurrency").value(DEF_CURRENCY_CODE))
                .andReturn();

        var parkingOccupations = parkingOccupationRepository.findAll();
        assertThat(parkingOccupations).hasSize(sizeBeforeTest);

        var parkingOccupation = parkingOccupations.get(parkingOccupations.size() - 1);

        assertThat(parkingOccupation).isNotNull();
        assertThat(parkingOccupation.getEndDate())
                .isNotNull()
                .isGreaterThan(mockParkingOccupation.getStartDate())
                .isLessThan(LocalDateTime.now());
        assertThat(parkingOccupation.getFeeAmount()).isEqualTo(DEF_FEE_AMOUNT);
        assertThat(parkingOccupation.getFeeCurrencyCode()).isEqualTo(DEF_CURRENCY_CODE);
    }

    @Test
    public void post_parkingOccupationStopURI_shouldReturnClientError_whenRegistrationNumberIsInvalid() throws Exception {
        mockMvc.perform(post(API_PARKING_OCCUPATIONS_STOP_URI)
                .contentType(MediaType.TEXT_PLAIN)
                .content(DEFAULT_REG_NUMBER))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Cannot found any active occupation for registrationNumber: " + DEFAULT_REG_NUMBER))
                .andReturn();
    }

    @Test
    public void get_parkingOccupationURI_shouldReturnParkingOccupation() throws Exception {
        var startDate = LocalDateTime.now();
        var parkingOccupation = ParkingOccupation.builder()
                .registrationNumber(DEFAULT_REG_NUMBER)
                .startDate(startDate)
                .build();

        parkingOccupationRepository.save(parkingOccupation);

        mockMvc.perform(get(API_PARKING_OCCUPATIONS_URI + "/" + DEFAULT_REG_NUMBER)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.registrationNumber").value(DEFAULT_REG_NUMBER))
                .andExpect(jsonPath("$.startDate").value(FORMATTER.format(startDate)))
                .andExpect(jsonPath("$.endDate").doesNotExist())
                .andReturn();
    }
}