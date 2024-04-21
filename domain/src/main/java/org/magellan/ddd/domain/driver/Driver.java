package org.magellan.ddd.domain.driver;

import static org.magellan.ddd.domain.driver.DriverStatus.AVAILABLE;
import static org.magellan.ddd.domain.driver.DriverStatus.BUSY;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.EntityId;
import org.magellan.ddd.domain.application.events.ApplicationAcceptedEvent;

@Getter
@EqualsAndHashCode(of = "id")
public class Driver {

  @EntityId(routingKey = "driverId")
  private final DriverId id;
  private DriverStatus status;

  public Driver(DriverId id) {
    this.id = id;
    this.status = AVAILABLE;
  }

  public boolean isBusy() {
    return this.status == BUSY;
  }

  @EventSourcingHandler
  public void on(ApplicationAcceptedEvent event) {
    this.status = DriverStatus.BUSY;
  }

}
