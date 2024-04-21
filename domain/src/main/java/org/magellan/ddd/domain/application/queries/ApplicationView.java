package org.magellan.ddd.domain.application.queries;

import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.magellan.ddd.domain.vehicle.queries.VehicleTypeView;

@Getter
@Setter
@ToString
public class ApplicationView {

  private String id;
  private String routeId;
  private String driverId;
  private VehicleTypeView requiredVehicleType;
  private String status;
  private Instant createdDate;

}
