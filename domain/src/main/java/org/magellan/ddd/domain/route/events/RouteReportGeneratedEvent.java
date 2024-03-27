package org.magellan.ddd.domain.route.events;

import org.magellan.ddd.domain.route.RouteId;
import org.magellan.ddd.domain.route.commands.GenerateRouteReportCommand;

public record RouteReportGeneratedEvent(RouteId routeId) {

  public static RouteReportGeneratedEvent of(GenerateRouteReportCommand command) {
    return new RouteReportGeneratedEvent(command.routeId());
  }

}
