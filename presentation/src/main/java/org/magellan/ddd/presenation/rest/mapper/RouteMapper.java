package org.magellan.ddd.presenation.rest.mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import org.magellan.ddd.domain.route.commands.CreateRouteCommand;
import org.magellan.ddd.domain.route.queries.RouteView;
import org.magellan.ddd.domain.user.UserId;
import org.magellan.ddd.presenation.rest.RouteController.CreateRouteRequest;
import org.magellan.ddd.presenation.rest.RouteController.GetRouteDetailsResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = SPRING, uses = TimestampMapper.class)
public interface RouteMapper {

  @Mapping(target = "routeId", expression = "java(RouteId.newInstance())")
  CreateRouteCommand toCommand(CreateRouteRequest request);

  @Mapping(target = "value", source = "id")
  UserId toTypedId(String id);

  @Mapping(target = "departureDate", source = "schedule.departureDate")
  @Mapping(target = "arrivalDate", source = "schedule.arrivalDate")
  GetRouteDetailsResponse toResponse(RouteView route);

}
