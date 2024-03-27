package org.magellan.ddd.domain.route.sagas;

import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.magellan.ddd.domain.route.commands.GenerateRouteReportCommand;
import org.magellan.ddd.domain.route.commands.SendRouteReportCommand;
import org.magellan.ddd.domain.route.events.RouteCompletedEvent;
import org.magellan.ddd.domain.route.events.RouteReportGeneratedEvent;

@Saga
@RequiredArgsConstructor
public class RouteReportSaga {

  private final transient CommandGateway gateway;

  @StartSaga
  @SagaEventHandler(associationProperty = "routeId")
  public void on(RouteCompletedEvent event) {
    gateway.send(new GenerateRouteReportCommand(event.routeId()));
  }

  @EndSaga
  @SagaEventHandler(associationProperty = "routeId")
  public void on(RouteReportGeneratedEvent event) {
    gateway.send(new SendRouteReportCommand(event.routeId()));
  }

}
