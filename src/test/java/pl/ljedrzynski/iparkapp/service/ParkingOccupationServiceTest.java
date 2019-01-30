package pl.ljedrzynski.iparkapp.service;

import org.junit.Before;
import org.junit.Test;
import pl.ljedrzynski.iparkapp.common.exception.OccupationAlreadyExistsException;


public class ParkingOccupationServiceTest {


    private ParkingOccupationService parkingOccupationService;


    @Before
    public void setUp() throws Exception {
    }

    @Test(expected = OccupationAlreadyExistsException.class)
    public void registerOccupation_shouldThrowException_whenOccupationExists() {

    }


}