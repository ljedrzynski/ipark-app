package pl.ljedrzynski.iparkapp.web.rest.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.ljedrzynski.iparkapp.common.Constants;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StartOccupationRequest {
    @NotNull
    @Pattern(regexp = Constants.REG_NUMBER_REGEXP)
    private String registrationNumber;
    private Boolean isVip;
}

