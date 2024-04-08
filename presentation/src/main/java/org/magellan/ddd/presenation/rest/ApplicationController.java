package org.magellan.ddd.presenation.rest;

import static org.springframework.http.HttpStatus.CREATED;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.magellan.ddd.domain.application.commands.AcceptApplicationCommand;
import org.magellan.ddd.domain.application.commands.SubmitApplicationCommand;
import org.magellan.ddd.presenation.rest.mapper.ApplicationCommandMapper;
import org.springframework.http.ResponseEntity;
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
  private final ApplicationCommandMapper commandMapper;

  @PostMapping
  public ResponseEntity<Void> submitApplication(@Valid @RequestBody SubmitApplicationRequest request) {
    log.debug("Submit application: {}", request);
    SubmitApplicationCommand command = commandMapper.toCommand(request);
    commandGateway.sendAndWait(command);
    return ResponseEntity.status(CREATED).build();
  }

  @PatchMapping("/{applicationId}/acceptance")
  public ResponseEntity<Void> submitRoute(@PathVariable("applicationId") String applicationId,
                                          @Valid @RequestBody AcceptApplicationRequest request) {
    log.debug("Accept application: {}", request);
    AcceptApplicationCommand command = commandMapper.toCommand(request, applicationId);
    commandGateway.sendAndWait(command);
    return ResponseEntity.accepted().build();
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

}
