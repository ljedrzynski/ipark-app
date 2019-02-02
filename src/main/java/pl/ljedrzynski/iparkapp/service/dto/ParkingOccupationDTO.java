package pl.ljedrzynski.iparkapp.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ParkingOccupationDTO {

    private Long id;
    @NotNull
    @Pattern(regexp = "^[A-Z]{1,3}([A-Z0-9]){1,5}$")
    private String registrationNumber;
    private Boolean isVip;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

}
