package pl.ljedrzynski.iparkapp.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "parking_fee_rate")
@Entity
public class ParkingFeeRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "currency_code")
    private String currencyCode;

    @Column(name = "first_hour_fee")
    private Double firstHourFee;

    @Column(name = "second_hour_fee")
    private Double secondHourFee;

    @Column(name = "next_hour_factor")
    private Double nextHourFactor;

    @Column(name = "vip")
    private Boolean isVip;

}
