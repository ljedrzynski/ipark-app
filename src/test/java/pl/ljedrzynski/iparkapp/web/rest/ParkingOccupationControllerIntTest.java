package pl.ljedrzynski.iparkapp.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@WebAppConfiguration
@SpringBootTest(classes = IparkAppApplication.class)
public class ParkingOccupationControllerIntTest {

    private static final String API_PARKING_OCCUPATIONS = "/api/parking-occupations";
    private static final String REGISTRATION_NUMBER = "registrationNumber";
    private static final String IS_VIP = "isVip";
    private static final String START_DATE = "startDate";
    private static final String DEFAULT_REG_NUMBER = "WWL 500123";
    private static final String APPLICATION_JSON_CHARSET_UTF_8 = "application/json;charset=UTF-8";
    public static final String OCCUPATION_ID = "occupationId";
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
    public void tearDown() {
    }

    @Test
    public void parkingOccupationURI_shouldCreateOccupation_whenMockMVC() throws Exception {
        ParkingOccupationDTO parkingOccupationDTO = getParkingOccupationDTO();
        int occupationsBeforeTest = parkingOccupationRepository.findAll().size();

        mockMvc.perform(post(API_PARKING_OCCUPATIONS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(parkingOccupationDTO)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        List<ParkingOccupation> occupations = parkingOccupationRepository.findAll();
        assertThat(occupations).hasSize(occupationsBeforeTest + 1);
        ParkingOccupation parkingOccupation = occupations.get(occupations.size() - 1);
        assertThat(parkingOccupation)
                .hasFieldOrPropertyWithValue(REGISTRATION_NUMBER, DEFAULT_REG_NUMBER)
                .hasFieldOrPropertyWithValue(IS_VIP, false)
                .hasFieldOrProperty(OCCUPATION_ID)
                .hasFieldOrProperty(START_DATE);
    }

    private ParkingOccupationDTO getParkingOccupationDTO() {
        return ParkingOccupationDTO.builder()
                .registrationNumber(DEFAULT_REG_NUMBER)
                .isVip(false)
                .build();
    }

}