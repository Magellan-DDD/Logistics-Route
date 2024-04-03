package org.magellan.ddd.domain.application;

import static org.magellan.ddd.domain.application.ApplicationStatus.REJECTED;

import java.time.Instant;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.EntityId;
import org.magellan.ddd.domain.application.commands.AcceptApplicationCommand;
import org.magellan.ddd.domain.application.events.ApplicationAcceptedEvent;
import org.magellan.ddd.domain.user.UserId;
import org.magellan.ddd.domain.vehicle.VehicleTypeId;

@Slf4j
@Builder
@Getter
@EqualsAndHashCode(of = "id")
public class Application {

  @EntityId(routingKey = "applicationId")
  private ApplicationId id;
  private UserId driverId;
  private VehicleTypeId requiredVehicleTypeId;
  private ApplicationStatus status;
  private Instant createdDate;

  public void reject() {
    this.status = REJECTED;
  }

  public void handle(AcceptApplicationCommand command) {
    if (this.status == ApplicationStatus.ACCEPTED) {
      log.warn("Application {} is already accepted for route {}", this.id, command.routeId());
    }
  }

  @EventSourcingHandler
  public void on(ApplicationAcceptedEvent event) {
    this.status = event.status();
  }

}
