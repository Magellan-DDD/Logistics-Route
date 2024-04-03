package org.magellan.ddd.infrastructure.persistence.jpa.route.entities;

import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Getter
@Setter
@Embeddable
public class ScheduleEntity {

  @Column(name = "DEPARTURE_DATE")
  private Instant departureDate;

  @Column(name = "ARRIVAL_DATE")
  private Instant arrivalDate;

}
