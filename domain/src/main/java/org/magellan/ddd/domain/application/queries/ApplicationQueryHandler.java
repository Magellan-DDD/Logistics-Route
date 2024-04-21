package org.magellan.ddd.domain.application.queries;

import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.magellan.ddd.domain.application.repositories.ApplicationRepository;
import org.magellan.ddd.domain.vehicle.accessors.VehicleAccessor;
import org.magellan.ddd.domain.vehicle.queries.VehicleTypeView;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApplicationQueryHandler {

  private final ApplicationRepository applicationRepository;
  private final VehicleAccessor vehicleAccessor;

  @QueryHandler
  public ApplicationView handle(GetApplicationDetailsQuery query) {
    ApplicationView application = applicationRepository.getById(query.applicationId());
    enrichVehicleType(application.getRequiredVehicleType());
    return application;
  }

  private void enrichVehicleType(VehicleTypeView vehicleType) {
    String vehicleTypeName = vehicleAccessor.getVehicleTypeName(vehicleType.getId());
    vehicleType.setName(vehicleTypeName);
  }

}
