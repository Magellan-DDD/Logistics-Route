package org.magellan.ddd.domain.application.queries;

import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public final class ApplicationView {

  private String id;
  private String routeId;
  private String driverId;
  private Integer requiredVehicleTypeId;
  private String status;
  private Instant createdDate;

}
