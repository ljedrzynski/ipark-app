package pl.ljedrzynski.iparkapp.web.rest;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
        ParkingOccupationDTO occupationDTO = parkingOccupationService.startOccupation(startOccupationRequest.getRegistrationNumber(), BooleanUtils.isTrue(startOccupationRequest.getIsVip()));
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .body(occupationDTO);


    }

    @PostMapping("/stop")
    public void stopOccupation(@RequestBody @Valid @Pattern(regexp = Constants.REG_NUMBER_REGEXP) String registrationNumber) {
        log.debug("REST request to stop occupation : {}", registrationNumber);
        parkingOccupationService.stopOccupation(registrationNumber);
    }


}
