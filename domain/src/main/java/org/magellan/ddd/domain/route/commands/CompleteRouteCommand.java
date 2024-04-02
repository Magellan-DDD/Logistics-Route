package org.magellan.ddd.domain.route.commands;

import java.time.Instant;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.magellan.ddd.domain.route.RouteId;
import jakarta.validation.constraints.NotNull;

public record CompleteRouteCommand(
    @TargetAggregateIdentifier
    RouteId routeId,
    @NotNull
    Instant actualArrivalDate) {

  public CompleteRouteCommand(RouteId routeId) {
    this(routeId, Instant.now());
  }
}
