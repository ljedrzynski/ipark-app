package pl.ljedrzynski.iparkapp.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import pl.ljedrzynski.iparkapp.IparkAppApplication;
import pl.ljedrzynski.iparkapp.domain.ParkingOccupation;
import pl.ljedrzynski.iparkapp.repository.ParkingOccupationRepository;
import pl.ljedrzynski.iparkapp.service.dto.ParkingOccupationDTO;
import pl.ljedrzynski.iparkapp.web.rest.request.StartOccupationRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.ljedrzynski.iparkapp.utils.TestUtils.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@WebAppConfiguration
@SpringBootTest(classes = IparkAppApplication.class)
public class ParkingOccupationResourceIntTest {

    private static final String API_PARKING_OCCUPATIONS_URI = "/api/parking-occupations";
    private static final String API_PARKING_OCCUPATIONS_START_URI = API_PARKING_OCCUPATIONS_URI + "/start";
    private static final String API_PARKING_OCCUPATIONS_STOP_URI = API_PARKING_OCCUPATIONS_URI + "/stop";
    private static final String DEFAULT_REG_NUMBER = "WWL50012";

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private ParkingOccupationRepository parkingOccupationRepository;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @After
    public void after() {
        parkingOccupationRepository.deleteAll();
    }

    @Test
    public void parkingOccupationStartURI_shouldCreateOccupation_whenMockMVC() throws Exception {
        int occupationsBeforeTest = parkingOccupationRepository.findAll().size();

        mockMvc.perform(post(API_PARKING_OCCUPATIONS_START_URI)
                .contentType(APPLICATION_JSON_CHARSET_UTF_8)
                .content(new ObjectMapper().writeValueAsString(getStartOccupationRequest())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_CHARSET_UTF_8))
                .andReturn();

        List<ParkingOccupation> occupations = parkingOccupationRepository.findAll();
        assertThat(occupations).hasSize(occupationsBeforeTest + 1);
        ParkingOccupation parkingOccupation = occupations.get(occupations.size() - 1);
        assertThat(parkingOccupation)
                .hasFieldOrPropertyWithValue(REGISTRATION_NUMBER, DEFAULT_REG_NUMBER)
                .hasFieldOrPropertyWithValue(IS_VIP, false)
                .hasFieldOrProperty(START_DATE);
    }

    @Test
    public void parkingOccupationStartURI_shouldReturnServerError_whenRegistrationNumberIsNull() throws Exception {
        ParkingOccupationDTO parkingOccupationDTO = new ParkingOccupationDTO();

        mockMvc.perform(post(API_PARKING_OCCUPATIONS_START_URI)
                .contentType(APPLICATION_JSON_CHARSET_UTF_8)
                .content(new ObjectMapper().writeValueAsString(parkingOccupationDTO)))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(APPLICATION_JSON_CHARSET_UTF_8))
                .andExpect(jsonPath("$.message").value("registrationNumber: must not be null"))
                .andReturn();
    }

    @Test
    public void parkingOccupationStartURI_shouldReturnServerError_whenRegistrationNumberIsInvalid() throws Exception {
        StartOccupationRequest startOccupationRequest = StartOccupationRequest.builder()
                .registrationNumber("0000")
                .build();

        mockMvc.perform(post(API_PARKING_OCCUPATIONS_START_URI)
                .contentType(APPLICATION_JSON_CHARSET_UTF_8)
                .content(new ObjectMapper().writeValueAsString(startOccupationRequest)))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(APPLICATION_JSON_CHARSET_UTF_8))
                .andExpect(jsonPath("$.message").value("registrationNumber: must match \"^[A-Z]{1,3}([A-Z0-9]){1,5}$\""))
                .andReturn();
    }

    @Test
    public void parkingOccupationStartURI_shouldReturnClientError_whenOccupationIsAlreadyActive() throws Exception {
        parkingOccupationRepository.save(
                ParkingOccupation.builder()
                        .id(1L)
                        .registrationNumber(DEFAULT_REG_NUMBER)
                        .startDate(LocalDateTime.now())
                        .build()
        );
        mockMvc.perform(post(API_PARKING_OCCUPATIONS_START_URI)
                .contentType(APPLICATION_JSON_CHARSET_UTF_8)
                .content(new ObjectMapper().writeValueAsString(getStartOccupationRequest())))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(APPLICATION_JSON_CHARSET_UTF_8))
                .andExpect(jsonPath("$.message").value("Parking occupation already registered and active"))
                .andReturn();
    }

    @Test
    public void parkingOccupationStopURI_shouldStopOccupation() throws Exception {
        LocalDateTime startTime = LocalDateTime.now();
        parkingOccupationRepository.save(
                ParkingOccupation.builder()
                        .id(1L)
                        .registrationNumber(DEFAULT_REG_NUMBER)
                        .startDate(startTime)
                        .build()
        );
        mockMvc.perform(post(API_PARKING_OCCUPATIONS_STOP_URI)
                .contentType(MediaType.TEXT_PLAIN)
                .content(DEFAULT_REG_NUMBER))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        ParkingOccupation parkingOccupation = parkingOccupationRepository.findById(1L).get();
        assertThat(parkingOccupation.getEndDate())
                .isNotNull()
                .isGreaterThan(startTime)
                .isLessThan(LocalDateTime.now());
    }

    @Test
    public void parkingOccupationStopURI_shouldReturnClientError_whenRegistrationNumberIsInvalid() throws Exception {
        mockMvc.perform(post(API_PARKING_OCCUPATIONS_STOP_URI)
                .contentType(MediaType.TEXT_PLAIN)
                .content(DEFAULT_REG_NUMBER))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("Parking occupation cannot be found for registrationNumber: " + DEFAULT_REG_NUMBER))
                .andReturn();
    }

    private StartOccupationRequest getStartOccupationRequest() {
        return StartOccupationRequest.builder()
                .registrationNumber(ParkingOccupationResourceIntTest.DEFAULT_REG_NUMBER)
                .isVip(false)
                .build();
    }

}