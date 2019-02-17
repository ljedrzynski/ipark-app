package pl.ljedrzynski.iparkapp.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
@Data
@ConfigurationProperties(prefix = "fee")
public class ParkingFeeProperties {

    private String defaultCurrency;

}
