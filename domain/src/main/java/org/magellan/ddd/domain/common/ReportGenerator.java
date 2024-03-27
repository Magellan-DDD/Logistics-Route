package org.magellan.ddd.domain.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.magellan.ddd.domain.route.commands.GenerateRouteReportCommand;
import org.magellan.ddd.domain.route.events.RouteReportGeneratedEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReportGenerator {

  private final EventGateway gateway;

  @CommandHandler
  public void handle(GenerateRouteReportCommand command) {
    log.info("Generate report for the route {}", command.routeId());
    gateway.publish(RouteReportGeneratedEvent.of(command));
  }

}
