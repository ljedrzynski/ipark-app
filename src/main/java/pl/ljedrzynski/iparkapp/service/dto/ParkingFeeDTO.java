package pl.ljedrzynski.iparkapp.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParkingFeeDTO {

    private double feeAmount;
    private String feeCurrency;
}
