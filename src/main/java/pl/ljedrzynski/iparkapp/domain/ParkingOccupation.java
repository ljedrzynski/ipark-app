package pl.ljedrzynski.iparkapp.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.ljedrzynski.iparkapp.common.Constants;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "parking_occupation")
public class ParkingOccupation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Pattern(regexp = Constants.REG_NUMBER_REGEXP)
    private String registrationNumber;
    @Column
    private Boolean isVip;
    @NotNull
    @Column
    private LocalDateTime startDate;
    @Column
    private LocalDateTime endDate;
}
