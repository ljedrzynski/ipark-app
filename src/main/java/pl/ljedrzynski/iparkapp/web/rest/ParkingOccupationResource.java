package pl.ljedrzynski.iparkapp.web.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.ljedrzynski.iparkapp.service.ParkingOccupationService;
import pl.ljedrzynski.iparkapp.service.dto.ParkingOccupationDTO;

import javax.validation.Valid;
import java.net.URI;

@Slf4j
@RestController
@RequestMapping(ParkingOccupationResource.API_PARKING_OCCUPATIONS_URI)
public class ParkingOccupationResource {

    static final String API_PARKING_OCCUPATIONS_URI = "/api/parking-occupations";

    private ParkingOccupationService parkingOccupationService;

    public ParkingOccupationResource(ParkingOccupationService parkingOccupationService) {
        this.parkingOccupationService = parkingOccupationService;
    }

    @PostMapping
    public ResponseEntity<?> registerOccupation(@RequestBody @Valid ParkingOccupationDTO parkingOccupationDTO) {
        log.debug("REST request : {}", parkingOccupationDTO);
        ParkingOccupationDTO occupationDTO = parkingOccupationService.createOccupation(parkingOccupationDTO);
        return ResponseEntity.created(URI.create(String.format("%s/%d", API_PARKING_OCCUPATIONS_URI, occupationDTO.getId())))
                .contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .body(occupationDTO);
    }
}
