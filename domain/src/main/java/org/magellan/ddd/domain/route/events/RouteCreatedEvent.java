package org.magellan.ddd.domain.route.events;

import org.magellan.ddd.domain.dispatcher.DispatcherId;
import org.magellan.ddd.domain.route.Address;
import org.magellan.ddd.domain.route.RouteId;
import org.magellan.ddd.domain.route.RouteStatus;
import org.magellan.ddd.domain.route.Schedule;
import org.magellan.ddd.domain.route.commands.CreateRouteCommand;

public record RouteCreatedEvent(
    RouteId routeId,
    RouteStatus status,
    DispatcherId dispatcherId,
    Address address,
    Schedule schedule) {

  public static RouteCreatedEvent of(CreateRouteCommand command) {
    return new RouteCreatedEvent(command.routeId(),
        RouteStatus.NEW,
        command.dispatcherId(),
        command.address(),
        command.schedule());
  }

}
