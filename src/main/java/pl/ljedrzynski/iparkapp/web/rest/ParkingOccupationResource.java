package pl.ljedrzynski.iparkapp.web.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.ljedrzynski.iparkapp.service.ParkingOccupationService;
import pl.ljedrzynski.iparkapp.service.dto.ParkingOccupationDTO;

import java.net.URI;

@Slf4j
@RestController
@RequestMapping(ParkingOccupationResource.API_PARKING_OCCUPATIONS)
public class ParkingOccupationResource {

    public static final String API_PARKING_OCCUPATIONS = "/api/parking-occupations";

    private ParkingOccupationService parkingOccupationService;

    public ParkingOccupationResource(ParkingOccupationService parkingOccupationService) {
        this.parkingOccupationService = parkingOccupationService;
    }

    @PostMapping
    public ResponseEntity<?> registerOccupation(@RequestBody ParkingOccupationDTO parkingOccupationDTO) {
        log.debug("REST request : {}", parkingOccupationDTO);
        ParkingOccupationDTO occupationDTO = parkingOccupationService.saveOccupation(parkingOccupationDTO);
        return ResponseEntity.created(URI.create(API_PARKING_OCCUPATIONS + "/" + occupationDTO.getId()))
                .body(occupationDTO);
    }
}
