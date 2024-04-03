package org.magellan.ddd.domain.route;

import static java.util.function.Predicate.not;
import static lombok.AccessLevel.PROTECTED;
import static org.magellan.ddd.domain.route.RouteStatus.COMPLETED;
import static org.magellan.ddd.domain.route.RouteStatus.NEW;
import static org.magellan.ddd.domain.route.RouteStatus.STARTED;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateEntityNotFoundException;
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
@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = PROTECTED)
public class Route {

  @AggregateIdentifier
  private RouteId id;
  private UserId driverId;
  private UserId dispatcherId;
  private VehicleId vehicleId;
  private Address address;
  private Schedule schedule;

  private Instant startedDate;
  private Instant actualArrivalDate;

  private RouteStatus status;

  @AggregateMember
  private Map<ApplicationId, Application> applications;

  @CommandHandler
  public Route(CreateRouteCommand command) {
    AggregateLifecycle.apply(RouteCreatedEvent.of(command));
  }

  @EventSourcingHandler
  public void on(RouteCreatedEvent event) {
    this.id = event.routeId();
    this.dispatcherId = event.dispatcherId();
    this.address = event.address();
    this.status = event.status();
    this.schedule = event.schedule();
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

  public void handle(AcceptApplicationCommand command) {
    Application application = getApplication(command.applicationId());
    UserId acceptedDriver = application.getDriverId();
    application.handle(command);
    AggregateLifecycle.apply(ApplicationAcceptedEvent.of(command, acceptedDriver));
  }

  @EventSourcingHandler
  public void on(ApplicationAcceptedEvent event) {
    this.vehicleId = event.vehicleId();
    this.driverId = event.driverId();
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
    this.startedDate = event.startedDate();
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
    this.actualArrivalDate = event.actualArrivalDate();
  }

  public UserId getApplicationDriverId(ApplicationId applicationId) {
    return getApplication(applicationId).getDriverId();
  }

  private Application getApplication(ApplicationId applicationId) {
    return Optional.ofNullable(this.applications.get(applicationId))
        .orElseThrow(() -> new AggregateEntityNotFoundException("Application %s is missing in the route %s"
            .formatted(applicationId, this.id)));
  }

}
