package org.magellan.ddd.domain.route.commands;

import java.time.Instant;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.magellan.ddd.domain.route.RouteId;
import jakarta.validation.constraints.NotNull;

public record StartRouteCommand(
    @TargetAggregateIdentifier
    RouteId routeId,
    @NotNull
    Instant departureDate) {

  public StartRouteCommand(RouteId routeId) {
    this(routeId, Instant.now());
  }
}
