package pl.ljedrzynski.iparkapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import pl.ljedrzynski.iparkapp.service.ParkingFeeService;
import pl.ljedrzynski.iparkapp.service.dto.ParkingOccupationDTO;

@SpringBootApplication
@EnableCaching
@EnableJpaRepositories
public class IparkAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(IparkAppApplication.class, args);
    }
}
