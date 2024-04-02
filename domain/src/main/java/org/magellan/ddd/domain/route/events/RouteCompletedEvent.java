package org.magellan.ddd.domain.route.events;

import java.time.Instant;
import org.magellan.ddd.domain.route.RouteId;
import org.magellan.ddd.domain.route.commands.CompleteRouteCommand;

public record RouteCompletedEvent(RouteId routeId, Instant actualArrivalDate) {

  public static RouteCompletedEvent of(CompleteRouteCommand command) {
    return new RouteCompletedEvent(command.routeId(), command.actualArrivalDate());
  }

}
