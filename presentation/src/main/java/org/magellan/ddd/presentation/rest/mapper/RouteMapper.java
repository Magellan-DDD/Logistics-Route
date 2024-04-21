package org.magellan.ddd.presentation.rest.mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;
import static org.mapstruct.NullValueCheckStrategy.ALWAYS;

import org.magellan.ddd.domain.route.commands.CreateRouteCommand;
import org.magellan.ddd.domain.route.queries.RouteView;
import org.magellan.ddd.domain.dispatcher.DispatcherId;
import org.magellan.ddd.presentation.rest.RouteController;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = SPRING, uses = TimestampMapper.class, nullValueCheckStrategy = ALWAYS)
public interface RouteMapper {

  @Mapping(target = "routeId", expression = "java(RouteId.newInstance())")
  CreateRouteCommand toCommand(RouteController.CreateRouteRequest request);

  @Mapping(target = "value", source = "id")
  DispatcherId toTypedId(String id);

  @Mapping(target = "departureDate", source = "schedule.departureDate")
  @Mapping(target = "arrivalDate", source = "schedule.arrivalDate")
  RouteController.GetRouteDetailsResponse toResponse(RouteView route);

}
