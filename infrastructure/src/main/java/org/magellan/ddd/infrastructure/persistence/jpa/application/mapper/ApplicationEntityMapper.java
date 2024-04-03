package org.magellan.ddd.infrastructure.persistence.jpa.application.mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import org.magellan.ddd.domain.application.queries.ApplicationView;
import org.magellan.ddd.infrastructure.persistence.jpa.application.entities.ApplicationEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = SPRING)
public interface ApplicationEntityMapper {

  ApplicationEntity toEntity(ApplicationView applicationView);

  ApplicationView toView(ApplicationEntity routeEntity);

}
