package org.magellan.ddd.domain.application.events;

import java.time.Instant;
import org.magellan.ddd.domain.application.ApplicationId;
import org.magellan.ddd.domain.application.ApplicationStatus;
import org.magellan.ddd.domain.application.commands.SubmitApplicationCommand;
import org.magellan.ddd.domain.driver.DriverId;
import org.magellan.ddd.domain.route.RouteId;
import org.magellan.ddd.domain.vehicle.VehicleTypeId;

public record ApplicationSubmittedEvent(
    RouteId routeId,
    ApplicationId applicationId,
    DriverId driverId,
    VehicleTypeId requiredVehicleTypeId,
    ApplicationStatus status,
    Instant createdDate) {

  public static ApplicationSubmittedEvent of(SubmitApplicationCommand command) {
    return new ApplicationSubmittedEvent(
        command.routeId(),
        command.applicationId(),
        command.driverId(),
        command.requiredVehicleTypeId(),
        command.status(),
        command.createdDate());
  }

}
