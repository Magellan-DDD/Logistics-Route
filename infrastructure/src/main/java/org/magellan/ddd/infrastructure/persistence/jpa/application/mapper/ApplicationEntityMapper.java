package org.magellan.ddd.infrastructure.persistence.jpa.application.mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import org.magellan.ddd.domain.application.queries.ApplicationView;
import org.magellan.ddd.infrastructure.persistence.jpa.application.entities.ApplicationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = SPRING)
public interface ApplicationEntityMapper {

  @Mapping(target = "vehicleTypeId", source = "requiredVehicleType.id")
  ApplicationEntity toEntity(ApplicationView applicationView);

  @Mapping(target = "requiredVehicleType.id", source = "vehicleTypeId")
  ApplicationView toView(ApplicationEntity routeEntity);

}
