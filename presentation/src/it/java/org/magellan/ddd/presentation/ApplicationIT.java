package org.magellan.ddd.presentation;

import static org.hamcrest.Matchers.is;
import static org.magellan.ddd.domain.application.ApplicationStatus.SUBMITTED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.time.Instant;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.junit.jupiter.api.Test;
import org.magellan.ddd.domain.application.ApplicationId;
import org.magellan.ddd.domain.application.commands.SubmitApplicationCommand;
import org.magellan.ddd.domain.driver.DriverId;
import org.magellan.ddd.domain.route.Address;
import org.magellan.ddd.domain.route.RouteId;
import org.magellan.ddd.domain.route.Schedule;
import org.magellan.ddd.domain.route.commands.CreateRouteCommand;
import org.magellan.ddd.domain.dispatcher.DispatcherId;
import org.magellan.ddd.domain.vehicle.VehicleTypeId;
import org.magellan.ddd.presentation.support.AbstractIntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;

public class ApplicationIT extends AbstractIntegrationTest {

  @Autowired
  private CommandGateway commandGateway;

  @Test
  void should_return_application_view() {
    RouteId routeId = commandGateway.sendAndWait(new CreateRouteCommand(
        new DispatcherId("6367437c-be39-4a45-9dae-54dc3766a6ac"),
        new Address("62701", "IL", "Springfield", "123 Main St", "Apt 1"),
        new Schedule(Instant.ofEpochSecond(1713470277), Instant.ofEpochSecond(1713475275))
    ));

    commandGateway.sendAndWait(new SubmitApplicationCommand(routeId,
        new ApplicationId("8ff6ead9-f838-4853-9f2e-d4413f44d3ea"),
        new DriverId("5557437c-be39-4a85-9dae-54dc37zxsa6ac"),
        new VehicleTypeId(1), SUBMITTED, Instant.ofEpochSecond(1713470277)));

    buildRequestSpecification().when()
        .contentType(APPLICATION_JSON_VALUE)
        .get("/api/v1/applications/8ff6ead9-f838-4853-9f2e-d4413f44d3ea")
        .then().statusCode(200)
        .body("id", is("8ff6ead9-f838-4853-9f2e-d4413f44d3ea"))
        .body("status", is("SUBMITTED"))
        .body("createdDate", is(1713470277))
        .body("driverId", is("5557437c-be39-4a85-9dae-54dc37zxsa6ac"))
        .body("routeId", is(routeId.toString()));
  }

}
