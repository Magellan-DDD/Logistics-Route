package org.magellan.ddd.domain.application.events;

import org.magellan.ddd.domain.application.ApplicationId;
import org.magellan.ddd.domain.application.ApplicationStatus;
import org.magellan.ddd.domain.application.commands.AcceptApplicationCommand;
import org.magellan.ddd.domain.route.RouteId;
import org.magellan.ddd.domain.vehicle.VehicleId;

public record ApplicationAcceptedEvent(
    RouteId routeId,
    ApplicationId applicationId,
    VehicleId vehicleId,
    ApplicationStatus status) {

  public static ApplicationAcceptedEvent of(AcceptApplicationCommand command) {
    return new ApplicationAcceptedEvent(
        command.routeId(),
        command.applicationId(),
        command.vehicleId(),
        command.status());
  }

}
