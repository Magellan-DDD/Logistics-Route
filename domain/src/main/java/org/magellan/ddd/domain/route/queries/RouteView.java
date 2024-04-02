package org.magellan.ddd.domain.route.queries;

import java.time.Instant;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class RouteView extends RouteBaseView {

  private String driverId;
  private String dispatcherId;
  private String vehicleId;
  private Instant startedDate;
  private Instant actualArrivalDate;
  private List<String> applicationIds;

}
