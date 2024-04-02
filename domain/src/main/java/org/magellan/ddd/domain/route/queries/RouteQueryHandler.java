package org.magellan.ddd.domain.route.queries;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.magellan.ddd.domain.route.repositories.RouteRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RouteQueryHandler {

  private final RouteRepository routeRepository;

  @QueryHandler
  public RouteView handle(GetRouteDetailsQuery query) {
    return routeRepository.getById(query.routeId());
  }

  @QueryHandler
  public List<RouteBaseView> handle(FetchRoutesQuery query) {
    return routeRepository.fetchBaseRouteViews();
  }

}
