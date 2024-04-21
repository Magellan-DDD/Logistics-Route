package org.magellan.ddd.presentation.rest;

import static org.springframework.http.HttpStatus.CREATED;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.magellan.ddd.domain.route.Address;
import org.magellan.ddd.domain.route.RouteId;
import org.magellan.ddd.domain.route.Schedule;
import org.magellan.ddd.domain.route.commands.CompleteRouteCommand;
import org.magellan.ddd.domain.route.commands.CreateRouteCommand;
import org.magellan.ddd.domain.route.commands.StartRouteCommand;
import org.magellan.ddd.domain.route.queries.FetchRoutesQuery;
import org.magellan.ddd.domain.route.queries.GetRouteDetailsQuery;
import org.magellan.ddd.domain.route.queries.RouteBaseView;
import org.magellan.ddd.domain.route.queries.RouteBaseView.AddressView;
import org.magellan.ddd.domain.route.queries.RouteView;
import org.magellan.ddd.presentation.rest.mapper.RouteMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Slf4j
@RestController
@RequestMapping("/api/v1/routes")
@RequiredArgsConstructor
public class RouteController {

  private final CommandGateway commandGateway;
  private final QueryGateway queryGateway;
  private final RouteMapper routeMapper;

  @PostMapping
  public CompletableFuture<ResponseEntity<RouteId>> createRoute(@Valid @RequestBody CreateRouteRequest request) {
    log.debug("Create route: {}", request);
    CreateRouteCommand command = routeMapper.toCommand(request);
    return commandGateway.<RouteId>send(command)
        .thenApply(ResponseEntity.status(CREATED)::body);
  }

  @PostMapping("/{routeId}/start")
  public CompletableFuture<RouteId> startRoute(@PathVariable("routeId") String routeId) {
    log.debug("Start route: {}", routeId);
    var command = new StartRouteCommand(new RouteId(routeId));
    return commandGateway.send(command);
  }

  @PostMapping("/{routeId}/complete")
  public CompletableFuture<RouteId> completeRoute(@PathVariable("routeId") String routeId) {
    log.debug("Complete route: {}", routeId);
    var command = new CompleteRouteCommand(new RouteId(routeId));
    return commandGateway.send(command);
  }

  @GetMapping
  public CompletableFuture<FetchRoutesResponse> fetchRoutes() {
    return queryGateway.query(new FetchRoutesQuery(), ResponseTypes.multipleInstancesOf(RouteBaseView.class))
        .thenApply(FetchRoutesResponse::new);
  }

  @GetMapping("/{routeId}")
  public CompletableFuture<GetRouteDetailsResponse> getRoute(@PathVariable("routeId") String routeId) {
    return queryGateway.query(new GetRouteDetailsQuery(routeId), ResponseTypes.instanceOf(RouteView.class))
        .thenApply(routeMapper::toResponse);
  }

  public record CreateRouteRequest(
      @NotBlank
      String dispatcherId,
      @NotNull
      Address address,
      @NotNull
      Schedule schedule) {
  }

  public record FetchRoutesResponse(List<RouteBaseView> routes) {
  }

  public record GetRouteDetailsResponse(
      String id,
      AddressView address,
      String status,
      String driverId,
      String dispatcherId,
      String vehicleId,
      Long startedDate,
      Long actualArrivalDate,
      Long departureDate,
      Long arrivalDate,
      List<String> applicationIds) {
  }

}
