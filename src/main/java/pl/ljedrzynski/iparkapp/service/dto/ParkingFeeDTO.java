package pl.ljedrzynski.iparkapp.service.dto;

import lombok.Data;

import javax.money.MonetaryAmount;

@Data
public class ParkingFeeDTO extends ParkingOccupationDTO {
    private MonetaryAmount fee;
}
