package org.magellan.ddd.domain.common;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.EventHandler;
import org.magellan.ddd.domain.route.commands.SendRouteReportCommand;
import org.magellan.ddd.domain.route.events.RouteCreatedEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NotificationSender {

  @EventHandler
  public void on(RouteCreatedEvent event) {
    log.info("Notify all drivers about the new route: {}", event.routeId());
  }

  @CommandHandler
  public void handle(SendRouteReportCommand command) {
    log.info("Send report for the route {}", command.routeId());
  }

}
