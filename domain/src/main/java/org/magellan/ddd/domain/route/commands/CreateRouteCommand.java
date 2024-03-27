package org.magellan.ddd.domain.route.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.magellan.ddd.domain.route.Address;
import org.magellan.ddd.domain.route.RouteId;
import org.magellan.ddd.domain.user.UserId;
import jakarta.validation.constraints.NotNull;

public record CreateRouteCommand(
    @TargetAggregateIdentifier
    RouteId routeId,
    @NotNull
    UserId dispatcherId,
    @NotNull
    Address address) {

  public CreateRouteCommand(UserId dispatcherId, Address address) {
    this(RouteId.newInstance(), dispatcherId, address);
  }
}
