package org.magellan.ddd.domain.route.repositories;

import java.util.List;
import org.magellan.ddd.domain.route.queries.RouteBaseView;
import org.magellan.ddd.domain.route.queries.RouteView;

public interface RouteRepository {

  void save(RouteView routeView);

  RouteView getById(String routeId);

  List<RouteBaseView> fetchBaseRouteViews();

}
