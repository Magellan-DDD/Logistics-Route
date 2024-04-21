package org.magellan.ddd.domain.route.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.magellan.ddd.domain.dispatcher.DispatcherId;
import org.magellan.ddd.domain.route.Address;
import org.magellan.ddd.domain.route.RouteId;
import org.magellan.ddd.domain.route.Schedule;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@Valid
public record CreateRouteCommand(
    @TargetAggregateIdentifier
    RouteId routeId,
    @NotNull
    DispatcherId dispatcherId,
    @NotNull
    Address address,
    @NotNull
    Schedule schedule) {

  public CreateRouteCommand(DispatcherId dispatcherId, Address address, Schedule schedule) {
    this(RouteId.newInstance(), dispatcherId, address, schedule);
  }
}
