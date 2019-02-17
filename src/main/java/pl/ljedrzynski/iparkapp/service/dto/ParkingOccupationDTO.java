package pl.ljedrzynski.iparkapp.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ParkingOccupationDTO {

    private String registrationNumber;

    private Boolean isVip;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    @JsonProperty("parkingFee")
    private ParkingFeeDTO parkingFeeDTO;

}
