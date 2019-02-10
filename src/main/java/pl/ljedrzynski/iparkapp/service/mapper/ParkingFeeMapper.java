package pl.ljedrzynski.iparkapp.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import pl.ljedrzynski.iparkapp.domain.ParkingOccupation;
import pl.ljedrzynski.iparkapp.service.dto.ParkingFeeDTO;

@Mapper(componentModel = "spring")
public interface ParkingFeeMapper {

    ParkingFeeMapper INSTANCE = Mappers.getMapper(ParkingFeeMapper.class);

    ParkingFeeDTO toParkingFeeDTO(ParkingOccupation parkingOccupation);
}
