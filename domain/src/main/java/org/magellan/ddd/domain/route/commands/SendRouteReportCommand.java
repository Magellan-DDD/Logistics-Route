package org.magellan.ddd.domain.route.commands;

import org.magellan.ddd.domain.route.RouteId;
import jakarta.validation.constraints.NotNull;

public record SendRouteReportCommand(@NotNull RouteId routeId) {

}
