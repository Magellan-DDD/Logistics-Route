package org.magellan.ddd.domain.application.mappers;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import org.magellan.ddd.domain.application.events.ApplicationSubmittedEvent;
import org.magellan.ddd.domain.application.queries.ApplicationView;
import org.magellan.ddd.domain.common.IdMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = SPRING)
public interface ApplicationViewMapper extends IdMapper {

  @Mapping(target = "id", source = "applicationId")
  @Mapping(target = "requiredVehicleType.id", source = "requiredVehicleTypeId")
  ApplicationView toView(ApplicationSubmittedEvent event);

}
