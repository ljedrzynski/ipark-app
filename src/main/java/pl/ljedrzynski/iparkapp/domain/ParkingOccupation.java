package pl.ljedrzynski.iparkapp.domain;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;


@Data
@Entity
@Table(name = "parking_occupation")
public class ParkingOccupation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Pattern(regexp = "^[A-Z]{1,3}([A-Z0-9]){1,5}$")
    private String registrationNumber;

    @Column
    private Boolean isVip;

    @NotNull
    @Column
    private LocalDateTime startDate;

    @Column
    private LocalDateTime endDate;

}
