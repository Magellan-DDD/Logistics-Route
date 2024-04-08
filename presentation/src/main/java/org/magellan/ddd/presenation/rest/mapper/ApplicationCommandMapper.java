package org.magellan.ddd.presenation.rest.mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import org.magellan.ddd.domain.application.ApplicationId;
import org.magellan.ddd.domain.application.commands.AcceptApplicationCommand;
import org.magellan.ddd.domain.application.commands.SubmitApplicationCommand;
import org.magellan.ddd.domain.route.RouteId;
import org.magellan.ddd.domain.user.UserId;
import org.magellan.ddd.domain.vehicle.VehicleId;
import org.magellan.ddd.domain.vehicle.VehicleTypeId;
import org.magellan.ddd.presenation.rest.ApplicationController.AcceptApplicationRequest;
import org.magellan.ddd.presenation.rest.ApplicationController.SubmitApplicationRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = SPRING)
public interface ApplicationCommandMapper {

  default SubmitApplicationCommand toCommand(SubmitApplicationRequest request) {
    return new SubmitApplicationCommand(
        new RouteId(request.routeId()),
        new UserId(request.driverId()),
        new VehicleTypeId(request.requiredVehicleTypeId())
    );
  }

  default AcceptApplicationCommand toCommand(AcceptApplicationRequest request, String applicationId) {
    return new AcceptApplicationCommand(
        new RouteId(request.routeId()),
        new ApplicationId(applicationId),
        new VehicleId(request.vehicleId())
    );
  }

}
