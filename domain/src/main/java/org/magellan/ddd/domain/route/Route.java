package org.magellan.ddd.domain.route;

import static java.util.function.Predicate.not;
import static org.magellan.ddd.domain.route.RouteStatus.COMPLETED;
import static org.magellan.ddd.domain.route.RouteStatus.NEW;
import static org.magellan.ddd.domain.route.RouteStatus.STARTED;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.modelling.command.AggregateMember;
import org.axonframework.spring.stereotype.Aggregate;
import org.magellan.ddd.domain.application.Application;
import org.magellan.ddd.domain.application.ApplicationId;
import org.magellan.ddd.domain.application.commands.AcceptApplicationCommand;
import org.magellan.ddd.domain.application.commands.SubmitApplicationCommand;
import org.magellan.ddd.domain.application.events.ApplicationAcceptedEvent;
import org.magellan.ddd.domain.application.events.ApplicationSubmittedEvent;
import org.magellan.ddd.domain.route.commands.CompleteRouteCommand;
import org.magellan.ddd.domain.route.commands.CreateRouteCommand;
import org.magellan.ddd.domain.route.commands.StartRouteCommand;
import org.magellan.ddd.domain.route.events.RouteCompletedEvent;
import org.magellan.ddd.domain.route.events.RouteCreatedEvent;
import org.magellan.ddd.domain.route.events.RouteStartedEvent;
import org.magellan.ddd.domain.user.UserId;
import org.magellan.ddd.domain.vehicle.VehicleId;

@Aggregate
@Getter
@EqualsAndHashCode(of = "id")
public class Route {

  @AggregateIdentifier
  private RouteId id;
  private UserId driverId;
  private UserId dispatcherId;
  private VehicleId vehicleId;
  private Address address;

  // todo move to schedule VO
  private Instant departureDate;
  private Instant arrivalDate;
  private Instant startedDate;

  private RouteStatus status;

  @AggregateMember
  private Map<ApplicationId, Application> applications;

  @CommandHandler
  public void handle(CreateRouteCommand command) {
    AggregateLifecycle.apply(RouteCreatedEvent.of(command));
  }

  @EventSourcingHandler
  public void on(RouteCreatedEvent event) {
    this.id = event.routeId();
    this.dispatcherId = event.dispatcherId();
    this.address = event.address();
    this.status = event.status();
    this.applications = new HashMap<>();
  }

  @CommandHandler
  public void handle(SubmitApplicationCommand command) {
    if (this.status != NEW) {
      throw new IllegalStateException("Cannot submit application %s to the route %s. Route status: %s"
          .formatted(command.applicationId(), this.id, this.status));
    }
    if (applications.containsKey(command.applicationId())) {
      throw new IllegalArgumentException("Application %s already submitted to the route %s"
          .formatted(command.applicationId(), this.id));
    }
    // todo check whether the driver didn't start another route
    AggregateLifecycle.apply(ApplicationSubmittedEvent.of(command));
  }

  @EventSourcingHandler
  public void on(ApplicationSubmittedEvent event) {
    this.applications.put(event.applicationId(), Application.builder()
        .id(event.applicationId())
        .driverId(event.driverId())
        .requiredVehicleTypeId(event.requiredVehicleTypeId())
        .status(event.status())
        .createdDate(event.createdDate())
        .build());
  }


  @CommandHandler
  public void handle(AcceptApplicationCommand command) {
    if (this.applications.containsKey(command.applicationId())) {
      AggregateLifecycle.apply(ApplicationAcceptedEvent.of(command));
    } else {
      throw new RuntimeException("Required application %s is missing".formatted(command.applicationId()));
    }
  }

  @EventSourcingHandler
  public void on(ApplicationAcceptedEvent event) {
    this.vehicleId = event.vehicleId();
    this.driverId = applications.get(event.applicationId()).getDriverId();
    this.applications.values().stream()
        .filter(not(app -> app.getId().equals(event.applicationId())))
        .forEach(Application::reject);
  }

  @CommandHandler
  public void handle(StartRouteCommand command) {
    if (this.status != NEW) {
      throw new IllegalStateException("Route is %s already started".formatted(this.id));
    }
    AggregateLifecycle.apply(RouteStartedEvent.of(command));
  }

  @EventSourcingHandler
  public void on(RouteStartedEvent event) {
    this.status = STARTED;
    // todo replace to startedDate
    this.departureDate = event.departureDate();
  }

  @CommandHandler
  public void handle(CompleteRouteCommand command) {
    if (this.status != STARTED) {
      throw new IllegalStateException("Route is %s already completed".formatted(this.id));
    }
    AggregateLifecycle.apply(RouteCompletedEvent.of(command));
  }

  @EventSourcingHandler
  public void on(RouteCompletedEvent event) {
    this.status = COMPLETED;
    this.arrivalDate = event.arrivalDate();
  }

}
