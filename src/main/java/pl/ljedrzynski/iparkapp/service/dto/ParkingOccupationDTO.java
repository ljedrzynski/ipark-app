package pl.ljedrzynski.iparkapp.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ParkingOccupationDTO {

    private Long id;
    private String registrationNumber;
    private Boolean isVip;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

}
