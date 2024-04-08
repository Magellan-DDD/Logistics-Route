package org.magellan.ddd.presenation.rest;

import static org.springframework.http.HttpStatus.CREATED;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.magellan.ddd.domain.route.Address;
import org.magellan.ddd.domain.route.RouteId;
import org.magellan.ddd.domain.route.Schedule;
import org.magellan.ddd.domain.route.commands.CompleteRouteCommand;
import org.magellan.ddd.domain.route.commands.CreateRouteCommand;
import org.magellan.ddd.domain.route.commands.StartRouteCommand;
import org.magellan.ddd.presenation.rest.mapper.RouteCommandMapper;
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
  private final RouteCommandMapper routeCommandMapper;

  @PostMapping
  public ResponseEntity<Void> createRoute(@Valid @RequestBody CreateRouteRequest request) {
    log.debug("Create route: {}", request);
    CreateRouteCommand command = routeCommandMapper.toCommand(request);
    commandGateway.sendAndWait(command);
    return ResponseEntity.status(CREATED).build();
  }

  @PostMapping("/{routeId}/start")
  public ResponseEntity<Void> submitRoute(@PathVariable("routeId") String routeId) {

    log.debug("Start route: {}", routeId);
    var command = new StartRouteCommand(new RouteId(routeId));
    commandGateway.sendAndWait(command);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/{routeId}/complete")
  public ResponseEntity<Void> completeRoute(@PathVariable("routeId") String routeId) {
    log.debug("Complete route: {}", routeId);
    var command = new CompleteRouteCommand(new RouteId(routeId));
    commandGateway.sendAndWait(command);
    return ResponseEntity.ok().build();
  }

  @GetMapping
  public void getRoutes() {

  }

  public record CreateRouteRequest(
      @NotBlank
      String dispatcherId,
      @NotNull
      Address address,
      @NotNull
      Schedule schedule) {
  }

}
