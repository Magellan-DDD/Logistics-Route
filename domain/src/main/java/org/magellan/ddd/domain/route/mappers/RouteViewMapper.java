package org.magellan.ddd.domain.route.mappers;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import org.magellan.ddd.domain.common.IdMapper;
import org.magellan.ddd.domain.route.events.RouteCreatedEvent;
import org.magellan.ddd.domain.route.queries.RouteView;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = SPRING)
public interface RouteViewMapper extends IdMapper {

  @Mapping(target = "id", source = "routeId")
  RouteView toView(RouteCreatedEvent event);

}
