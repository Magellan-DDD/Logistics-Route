package org.magellan.ddd.presentation;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.time.Instant;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.command.Repository;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;
import org.magellan.ddd.domain.route.Address;
import org.magellan.ddd.domain.route.Route;
import org.magellan.ddd.domain.route.RouteId;
import org.magellan.ddd.domain.route.Schedule;
import org.magellan.ddd.domain.route.commands.CreateRouteCommand;
import org.magellan.ddd.domain.dispatcher.DispatcherId;
import org.magellan.ddd.presentation.support.AbstractIntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;

public class RouteIT extends AbstractIntegrationTest {

  @Autowired
  private Repository<Route> routeRepository;

  @Autowired
  private CommandGateway commandGateway;

  @Test
  void should_create_route() {
    String routeId = buildRequestSpecification().when()
        .contentType(APPLICATION_JSON_VALUE)
        .body("""
            {
              "dispatcherId": "6367437c-be39-4a45-9dae-54dc3766a6ac",
              "address": {
                "street": "123 Main St",
                "city": "Springfield",
                "region": "IL",
                "zip": "62701",
                "building": "Apt 1"
              },
              "schedule": {
                "departureDate": "1713470277",
                "arrivalDate": "1713475275"
              }
            }
            """)
        .post("/api/v1/routes")
        .then().statusCode(201)
        .body("value", isUUID())
        .extract().jsonPath().getString("value");

    assertAgg(routeId, routeRepository, assertion -> assertion
        .hasFieldOrPropertyWithValue("dispatcherId", new DispatcherId("6367437c-be39-4a45-9dae-54dc3766a6ac"))
        .hasFieldOrPropertyWithValue("address.street", "123 Main St")
        .hasFieldOrPropertyWithValue("address.city", "Springfield")
        .hasFieldOrPropertyWithValue("address.region", "IL")
        .hasFieldOrPropertyWithValue("address.zip", "62701")
        .hasFieldOrPropertyWithValue("address.building", "Apt 1")
        .hasFieldOrPropertyWithValue("schedule.departureDate", Instant.ofEpochSecond(1713470277))
        .hasFieldOrPropertyWithValue("schedule.arrivalDate", Instant.ofEpochSecond(1713475275)));
  }

  @Test
  void should_return_route_view() {
    RouteId routeId = commandGateway.sendAndWait(new CreateRouteCommand(
        new DispatcherId("6367437c-be39-4a45-9dae-54dc3766a6ac"),
        new Address("62701", "IL", "Springfield", "123 Main St", "Apt 1"),
        new Schedule(Instant.ofEpochSecond(1713470277), Instant.ofEpochSecond(1713475275))
    ));

    buildRequestSpecification().when()
        .contentType(APPLICATION_JSON_VALUE)
        .get("/api/v1/routes/{routeId}", routeId.value())
        .then().statusCode(200)
        .body("id", is(routeId.value()))
        .body("status", is("NEW"))
        .body("dispatcherId", is("6367437c-be39-4a45-9dae-54dc3766a6ac"))
        .body("address.street", is("123 Main St"))
        .body("address.city", is("Springfield"))
        .body("address.region", is("IL"))
        .body("address.zip", is("62701"))
        .body("address.building", is("Apt 1"))
        .body("departureDate", is(1713470277))
        .body("arrivalDate", is(1713475275));
  }

  static Matcher<String> isUUID() {
    return matchesPattern("^[a-f0-9]{8}-[a-f0-9]{4}-4[a-f0-9]{3}-[89ab][a-f0-9]{3}-[a-f0-9]{12}$");
  }

}
