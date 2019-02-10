package pl.ljedrzynski.iparkapp.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import pl.ljedrzynski.iparkapp.domain.ParkingOccupation;
import pl.ljedrzynski.iparkapp.service.dto.ParkingOccupationDTO;

@Mapper(componentModel = "spring")
public interface ParkingOccupationMapper {

    ParkingOccupationMapper INSTANCE = Mappers.getMapper(ParkingOccupationMapper.class);

    ParkingOccupationDTO toDTO(ParkingOccupation parkingOccupation);

    ParkingOccupation toEntity(ParkingOccupationDTO parkingOccupationDTO);
}
