package pl.ljedrzynski.iparkapp.web.rest;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.ljedrzynski.iparkapp.common.Constants;
import pl.ljedrzynski.iparkapp.service.ParkingOccupationService;
import pl.ljedrzynski.iparkapp.service.dto.ParkingOccupationDTO;
import pl.ljedrzynski.iparkapp.web.rest.request.StartOccupationRequest;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

@Slf4j
@RestController
@RequestMapping(ParkingOccupationResource.API_PARKING_OCCUPATIONS_URI)
public class ParkingOccupationResource {

    static final String API_PARKING_OCCUPATIONS_URI = "/api/parking-occupations";

    private ParkingOccupationService parkingOccupationService;

    public ParkingOccupationResource(ParkingOccupationService parkingOccupationService) {
        this.parkingOccupationService = parkingOccupationService;
    }

    @PostMapping("/start")
    public ResponseEntity<ParkingOccupationDTO> startOccupation(@RequestBody @Valid StartOccupationRequest startOccupationRequest) {
        log.debug("REST request to start occupation : {}", startOccupationRequest);
        var occupationDTO = parkingOccupationService.startOccupation(startOccupationRequest.getRegistrationNumber(), BooleanUtils.isTrue(startOccupationRequest.getIsVip()));
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(occupationDTO);


    }

    @PostMapping("/stop")
    public void stopOccupation(@RequestBody @Valid @Pattern(regexp = Constants.REG_NUMBER_REGEXP) String regNumber) {
        log.debug("REST request to stop occupation : {}", regNumber);
        parkingOccupationService.stopOccupation(regNumber);
    }

    @GetMapping("/{regNumber}")
    public ResponseEntity<ParkingOccupationDTO> getParkingOccupation(@PathVariable String regNumber) {
        log.debug("REST request to get occupation : {}", regNumber);
        var parkingOccupation = parkingOccupationService.getParkingOccupation(regNumber);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(parkingOccupation);
    }

}
