package org.magellan.ddd.domain.application.commands;

import static org.magellan.ddd.domain.application.ApplicationStatus.ACCEPTED;

import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.magellan.ddd.domain.application.ApplicationId;
import org.magellan.ddd.domain.application.ApplicationStatus;
import org.magellan.ddd.domain.route.RouteId;
import org.magellan.ddd.domain.vehicle.VehicleId;
import jakarta.validation.constraints.NotNull;

public record AcceptApplicationCommand(

    @TargetAggregateIdentifier
    RouteId routeId,
    @NotNull
    ApplicationId applicationId,
    @NotNull
    VehicleId vehicleId,
    @NotNull
    ApplicationStatus status) {

  public AcceptApplicationCommand(RouteId routeId, ApplicationId applicationId, VehicleId vehicleId) {
    this(routeId, applicationId, vehicleId, ACCEPTED);
  }

}
