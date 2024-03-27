package org.magellan.ddd.domain.route.events;

import java.time.Instant;
import org.magellan.ddd.domain.route.RouteId;
import org.magellan.ddd.domain.route.commands.StartRouteCommand;

public record RouteStartedEvent(RouteId routeId, Instant departureDate) {

  public static RouteStartedEvent of(StartRouteCommand command) {
    return new RouteStartedEvent(command.routeId(), command.departureDate());
  }

}
