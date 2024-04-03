package org.magellan.ddd.domain.application.commands;

import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.modelling.command.Repository;
import org.magellan.ddd.domain.route.Route;
import org.magellan.ddd.domain.route.repositories.RouteRepository;
import org.magellan.ddd.domain.user.UserId;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AcceptApplicationCommandHandler {

  private final Repository<Route> aggregateRepository;
  private final RouteRepository viewRepository;

  @CommandHandler
  public void handle(AcceptApplicationCommand command) {
    aggregateRepository.load(command.routeId().toString())
        .execute(route -> {
          UserId driverId = route.getApplicationDriverId(command.applicationId());
          verifyDriverAvailability(driverId);
          route.handle(command);
        });
  }

  private void verifyDriverAvailability(UserId driverId) {
    if (!viewRepository.isDriverAvailable(driverId.value())) {
      throw new RuntimeException("Driver %s has been already assigned to another route");
    }
  }

}
