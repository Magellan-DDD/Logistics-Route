package org.magellan.ddd.domain.route.queries;

import static org.magellan.ddd.domain.route.RouteStatus.COMPLETED;
import static org.magellan.ddd.domain.route.RouteStatus.STARTED;

import lombok.RequiredArgsConstructor;
import org.axonframework.eventhandling.EventHandler;
import org.magellan.ddd.domain.application.events.ApplicationAcceptedEvent;
import org.magellan.ddd.domain.route.RouteId;
import org.magellan.ddd.domain.route.events.RouteCompletedEvent;
import org.magellan.ddd.domain.route.events.RouteCreatedEvent;
import org.magellan.ddd.domain.route.events.RouteStartedEvent;
import org.magellan.ddd.domain.route.mappers.RouteViewMapper;
import org.magellan.ddd.domain.route.repositories.RouteRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RouteEventHandler {

  private final RouteRepository routeRepository;
  private final RouteViewMapper routeViewMapper;

  @EventHandler
  public void on(RouteCreatedEvent event) {
    RouteView routeView = routeViewMapper.toView(event);
    routeRepository.save(routeView);
  }

  @EventHandler
  public void on(ApplicationAcceptedEvent event) {
    RouteView routeView = currentRoute(event.routeId());
    routeView.setDriverId(event.driverId().value());
    routeView.setVehicleId(event.vehicleId().value());
    routeRepository.save(routeView);
  }

  @EventHandler
  public void on(RouteStartedEvent event) {
    RouteView routeView = currentRoute(event.routeId());
    routeView.setStatus(STARTED.name());
    routeView.setStartedDate(event.startedDate());
    routeRepository.save(routeView);
  }

  @EventHandler
  public void on(RouteCompletedEvent event) {
    RouteView routeView = currentRoute(event.routeId());
    routeView.setStatus(COMPLETED.name());
    routeView.setActualArrivalDate(event.actualArrivalDate());
    routeRepository.save(routeView);
  }

  private RouteView currentRoute(RouteId routeId) {
    return routeRepository.getById(routeId.value());
  }

}
