package org.magellan.ddd.presentation.rest.mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import org.magellan.ddd.domain.application.ApplicationId;
import org.magellan.ddd.domain.application.commands.AcceptApplicationCommand;
import org.magellan.ddd.domain.application.commands.SubmitApplicationCommand;
import org.magellan.ddd.domain.application.queries.ApplicationView;
import org.magellan.ddd.domain.driver.DriverId;
import org.magellan.ddd.domain.route.RouteId;
import org.magellan.ddd.domain.vehicle.VehicleId;
import org.magellan.ddd.domain.vehicle.VehicleTypeId;
import org.magellan.ddd.presentation.rest.ApplicationController.AcceptApplicationRequest;
import org.magellan.ddd.presentation.rest.ApplicationController.GetApplicationDetailsResponse;
import org.magellan.ddd.presentation.rest.ApplicationController.SubmitApplicationRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = SPRING, uses = TimestampMapper.class)
public interface ApplicationMapper {

  default SubmitApplicationCommand toCommand(SubmitApplicationRequest request) {
    return new SubmitApplicationCommand(
        new RouteId(request.routeId()),
        new DriverId(request.driverId()),
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

  GetApplicationDetailsResponse toResponse(ApplicationView application);
}
