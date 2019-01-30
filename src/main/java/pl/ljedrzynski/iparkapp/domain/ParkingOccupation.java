package pl.ljedrzynski.iparkapp.domain;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "parking")
public class ParkingOccupation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 4, max = 10)
    @NotNull
    @Column(name = "reg_number")
    private String registrationNumber;

    @NotNull
    @Column
    private Boolean isVip;

    @NotNull
    @Column
    private LocalDateTime startDate;

    @Column
    private LocalDateTime endDate;

}
