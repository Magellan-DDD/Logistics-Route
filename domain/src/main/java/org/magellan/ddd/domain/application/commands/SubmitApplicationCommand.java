package org.magellan.ddd.domain.application.commands;

import static org.magellan.ddd.domain.application.ApplicationStatus.SUBMITTED;

import java.time.Instant;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.magellan.ddd.domain.application.ApplicationId;
import org.magellan.ddd.domain.application.ApplicationStatus;
import org.magellan.ddd.domain.driver.DriverId;
import org.magellan.ddd.domain.route.RouteId;
import org.magellan.ddd.domain.vehicle.VehicleTypeId;
import jakarta.validation.constraints.NotNull;

public record SubmitApplicationCommand(

    @TargetAggregateIdentifier
    RouteId routeId,
    @NotNull
    ApplicationId applicationId,
    @NotNull
    DriverId driverId,
    @NotNull
    VehicleTypeId requiredVehicleTypeId,
    @NotNull
    ApplicationStatus status,
    @NotNull
    Instant createdDate) {

  public SubmitApplicationCommand(RouteId routeId, DriverId driverId, VehicleTypeId requiredVehicleTypeId) {
    this(routeId, ApplicationId.newInstance(), driverId, requiredVehicleTypeId, SUBMITTED, Instant.now());
  }

}
