package org.magellan.ddd.presenation.rest;

import static org.springframework.http.HttpStatus.CREATED;

import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.magellan.ddd.domain.application.commands.AcceptApplicationCommand;
import org.magellan.ddd.domain.application.commands.SubmitApplicationCommand;
import org.magellan.ddd.domain.application.queries.ApplicationView;
import org.magellan.ddd.domain.application.queries.GetApplicationDetailsQuery;
import org.magellan.ddd.presenation.rest.mapper.ApplicationMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
@RequestMapping("/api/v1/applications")
@RequiredArgsConstructor
public class ApplicationController {

  private final CommandGateway commandGateway;
  private final QueryGateway queryGateway;
  private final ApplicationMapper applicationMapper;

  @PostMapping
  public CompletableFuture<ResponseEntity<Void>> submitApplication(@Valid @RequestBody SubmitApplicationRequest request) {
    log.debug("Submit application: {}", request);
    SubmitApplicationCommand command = applicationMapper.toCommand(request);
    return commandGateway.send(command)
        .thenApply(r -> ResponseEntity.status(CREATED).build());
  }

  @PatchMapping("/{applicationId}/acceptance")
  public CompletableFuture<Void> submitRoute(@PathVariable("applicationId") String applicationId,
                                             @Valid @RequestBody AcceptApplicationRequest request) {
    log.debug("Accept application: {}", request);
    AcceptApplicationCommand command = applicationMapper.toCommand(request, applicationId);
    return commandGateway.send(command);
  }

  @GetMapping("/{applicationId}")
  public CompletableFuture<GetApplicationDetailsResponse> getApplication(
      @PathVariable("applicationId") String applicationId) {

    var query = new GetApplicationDetailsQuery(applicationId);
    return queryGateway.query(query, ResponseTypes.instanceOf(ApplicationView.class))
        .thenApply(applicationMapper::toResponse);
  }

  public record SubmitApplicationRequest(
      @NotBlank
      String routeId,
      @NotNull
      String driverId,
      @NotNull
      Integer requiredVehicleTypeId) {
  }

  public record AcceptApplicationRequest(
      @NotBlank
      String routeId,
      @NotNull
      String vehicleId) {
  }

  public record GetApplicationDetailsResponse(
      String id,
      String routeId,
      String driverId,
      Integer requiredVehicleTypeId,
      String status,
      long createdDate) {
  }

}
