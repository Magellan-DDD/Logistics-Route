package org.magellan.ddd.domain.route;

import static org.magellan.ddd.domain.application.ApplicationStatus.ACCEPTED;
import static org.magellan.ddd.domain.application.ApplicationStatus.SUBMITTED;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Instant;
import org.axonframework.modelling.command.AggregateEntityNotFoundException;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.magellan.ddd.domain.application.ApplicationId;
import org.magellan.ddd.domain.application.commands.AcceptApplicationCommand;
import org.magellan.ddd.domain.application.commands.SubmitApplicationCommand;
import org.magellan.ddd.domain.application.events.ApplicationAcceptedEvent;
import org.magellan.ddd.domain.application.events.ApplicationSubmittedEvent;
import org.magellan.ddd.domain.dispatcher.DispatcherId;
import org.magellan.ddd.domain.driver.DriverId;
import org.magellan.ddd.domain.route.commands.CompleteRouteCommand;
import org.magellan.ddd.domain.route.commands.CreateRouteCommand;
import org.magellan.ddd.domain.route.commands.StartRouteCommand;
import org.magellan.ddd.domain.route.events.RouteCompletedEvent;
import org.magellan.ddd.domain.route.events.RouteCreatedEvent;
import org.magellan.ddd.domain.route.events.RouteStartedEvent;
import org.magellan.ddd.domain.route.repositories.RouteRepository;
import org.magellan.ddd.domain.vehicle.VehicleId;
import org.magellan.ddd.domain.vehicle.VehicleTypeId;

public class RouteAggregateTest {

  private static final RouteId ROUTE_ID = new RouteId("around-the-world-route");
  private static final DispatcherId DISPATCHER_ID = new DispatcherId("mrs-claus");
  private static final DriverId DRIVER_ID = new DriverId("santa-claus");
  private static final VehicleTypeId VEHICLE_TYPE_ID = new VehicleTypeId(777);
  private static final VehicleId VEHICLE_ID = new VehicleId("santa-sleigh");
  private static final ApplicationId APPLICATION_ID = new ApplicationId("santa-claus-application");
  private static final Address ADDRESS = new Address("58000", "Chernivtsi", "Chernivtsi", "Golovna", "5");
  private static final Schedule SCHEDULE = new Schedule(Instant.MIN, Instant.MAX);
  private static final Instant CREATED_DATE = Instant.parse("2024-12-23T00:00:00.00Z");
  private static final Instant STARTED_DATE = Instant.parse("2024-12-24T23:00:00.00Z");
  private static final Instant ARRIVAL_DATE = Instant.parse("2024-12-25T00:00:00.00Z");
  private FixtureConfiguration<Route> fixture;

  @BeforeEach
  void setUp() {
    fixture = new AggregateTestFixture<>(Route.class);
    var viewRepository = mock(RouteRepository.class);
    when(viewRepository.isDriverAvailable(DRIVER_ID.value())).thenReturn(true);
  }

  @Test
  void should_create_route() {
    fixture.givenNoPriorActivity()
        .when(new CreateRouteCommand(ROUTE_ID, DISPATCHER_ID, ADDRESS, SCHEDULE))
        .expectEvents(new RouteCreatedEvent(ROUTE_ID, RouteStatus.NEW, DISPATCHER_ID, ADDRESS, SCHEDULE));
  }

  @Test
  void should_submit_application() {
    fixture.given(new RouteCreatedEvent(ROUTE_ID, RouteStatus.NEW, DISPATCHER_ID, ADDRESS, SCHEDULE))
        .when(new SubmitApplicationCommand(ROUTE_ID, APPLICATION_ID, DRIVER_ID, VEHICLE_TYPE_ID, SUBMITTED, CREATED_DATE))
        .expectEvents(new ApplicationSubmittedEvent(ROUTE_ID, APPLICATION_ID, DRIVER_ID, VEHICLE_TYPE_ID, SUBMITTED, CREATED_DATE));
  }

  @Test
  void should_throw_invalid_route_status_exception() {
    fixture.given(new RouteCreatedEvent(ROUTE_ID, RouteStatus.STARTED, DISPATCHER_ID, ADDRESS, SCHEDULE))
        .when(new SubmitApplicationCommand(ROUTE_ID, APPLICATION_ID, DRIVER_ID, VEHICLE_TYPE_ID, SUBMITTED, CREATED_DATE))
        .expectException(IllegalStateException.class);
  }

  @Test
  void should_accept_application() {
    fixture.given(
            new RouteCreatedEvent(ROUTE_ID, RouteStatus.NEW, DISPATCHER_ID, ADDRESS, SCHEDULE),
            new ApplicationSubmittedEvent(ROUTE_ID, APPLICATION_ID, DRIVER_ID, VEHICLE_TYPE_ID, SUBMITTED, CREATED_DATE))
        .when(new AcceptApplicationCommand(ROUTE_ID, APPLICATION_ID, VEHICLE_ID))
        .expectEvents(new ApplicationAcceptedEvent(ROUTE_ID, APPLICATION_ID, VEHICLE_ID, ACCEPTED, DRIVER_ID));
  }

  @Test
  void should_throw_missing_application_exception() {
    var invalidApplicationId = ApplicationId.newInstance();
    fixture.given(
            new RouteCreatedEvent(ROUTE_ID, RouteStatus.NEW, DISPATCHER_ID, ADDRESS, SCHEDULE),
            new ApplicationSubmittedEvent(ROUTE_ID, APPLICATION_ID, DRIVER_ID, VEHICLE_TYPE_ID, SUBMITTED, CREATED_DATE))
        .when(new AcceptApplicationCommand(ROUTE_ID, invalidApplicationId, VEHICLE_ID))
        .expectException(AggregateEntityNotFoundException.class);
  }

  @Test
  void should_start_route() {
    fixture.given(
            new RouteCreatedEvent(ROUTE_ID, RouteStatus.NEW, DISPATCHER_ID, ADDRESS, SCHEDULE),
            new ApplicationSubmittedEvent(ROUTE_ID, APPLICATION_ID, DRIVER_ID, VEHICLE_TYPE_ID, SUBMITTED, CREATED_DATE),
            new ApplicationAcceptedEvent(ROUTE_ID, APPLICATION_ID, VEHICLE_ID, ACCEPTED, DRIVER_ID))
        .when(new StartRouteCommand(ROUTE_ID, STARTED_DATE))
        .expectEvents(new RouteStartedEvent(ROUTE_ID, STARTED_DATE));
  }

  @Test
  void should_throw_route_already_started_exception() {
    fixture.given(
            new RouteCreatedEvent(ROUTE_ID, RouteStatus.NEW, DISPATCHER_ID, ADDRESS, SCHEDULE),
            new ApplicationSubmittedEvent(ROUTE_ID, APPLICATION_ID, DRIVER_ID, VEHICLE_TYPE_ID, SUBMITTED, CREATED_DATE),
            new ApplicationAcceptedEvent(ROUTE_ID, APPLICATION_ID, VEHICLE_ID, ACCEPTED, DRIVER_ID),
            new RouteStartedEvent(ROUTE_ID, STARTED_DATE))
        .when(new StartRouteCommand(ROUTE_ID, STARTED_DATE))
        .expectException(IllegalStateException.class);
  }

  @Test
  void should_complete_route() {
    fixture.given(
            new RouteCreatedEvent(ROUTE_ID, RouteStatus.NEW, DISPATCHER_ID, ADDRESS, SCHEDULE),
            new ApplicationSubmittedEvent(ROUTE_ID, APPLICATION_ID, DRIVER_ID, VEHICLE_TYPE_ID, SUBMITTED, CREATED_DATE),
            new ApplicationAcceptedEvent(ROUTE_ID, APPLICATION_ID, VEHICLE_ID, ACCEPTED, DRIVER_ID),
            new RouteStartedEvent(ROUTE_ID, STARTED_DATE))
        .when(new CompleteRouteCommand(ROUTE_ID, ARRIVAL_DATE))
        .expectEvents(new RouteCompletedEvent(ROUTE_ID, ARRIVAL_DATE));
  }

  @Test
  void should_throw_route_already_completed_exception() {
    fixture.given(
            new RouteCreatedEvent(ROUTE_ID, RouteStatus.NEW, DISPATCHER_ID, ADDRESS, SCHEDULE),
            new ApplicationSubmittedEvent(ROUTE_ID, APPLICATION_ID, DRIVER_ID, VEHICLE_TYPE_ID, SUBMITTED, CREATED_DATE),
            new ApplicationAcceptedEvent(ROUTE_ID, APPLICATION_ID, VEHICLE_ID, ACCEPTED, DRIVER_ID),
            new RouteStartedEvent(ROUTE_ID, STARTED_DATE),
            new RouteCompletedEvent(ROUTE_ID, ARRIVAL_DATE))
        .when(new CompleteRouteCommand(ROUTE_ID, ARRIVAL_DATE))
        .expectException(IllegalStateException.class);
  }

}
