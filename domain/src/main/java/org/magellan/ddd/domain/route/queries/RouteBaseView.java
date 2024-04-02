package org.magellan.ddd.domain.route.queries;

import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RouteBaseView {
  private String id;
  private AddressView address;
  private ScheduleView schedule;
  private String status;

  public record AddressView(
      String zip,
      String region,
      String city,
      String street,
      String building) {
  }

  public record ScheduleView(Instant departureDate, Instant arrivalDate) {
  }

}
