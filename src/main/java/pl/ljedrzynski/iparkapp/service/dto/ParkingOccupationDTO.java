package pl.ljedrzynski.iparkapp.service.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ParkingOccupationDTO {

    private String registrationNumber;
    private Boolean isVip;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
