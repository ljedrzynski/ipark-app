package pl.ljedrzynski.iparkapp.service.converter;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import pl.ljedrzynski.iparkapp.domain.ParkingOccupation;
import pl.ljedrzynski.iparkapp.service.dto.ParkingOccupationDTO;

@Mapper(componentModel = "spring")
public interface ParkingMapper {

    ParkingMapper INSTANCE = Mappers.getMapper(ParkingMapper.class);

    ParkingOccupationDTO parkingToParkingDTO(ParkingOccupation parkingOccupation);

    ParkingOccupation parkingDTOtoParking(ParkingOccupationDTO parkingOccupationDTO);
}
