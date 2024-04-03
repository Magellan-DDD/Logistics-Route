package org.magellan.ddd.infrastructure.persistence.jpa.route;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.magellan.ddd.domain.route.queries.RouteBaseView;
import org.magellan.ddd.domain.route.queries.RouteView;
import org.magellan.ddd.domain.route.repositories.RouteRepository;
import org.magellan.ddd.infrastructure.persistence.jpa.route.entities.RouteEntity;
import org.magellan.ddd.infrastructure.persistence.jpa.route.mappers.RouteEntityMapper;
import org.springframework.stereotype.Repository;
import jakarta.annotation.PostConstruct;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RoutDAO implements RouteRepository {

  private static final String STARTED_STATUS = "STARTED";

  private final RouteJpaRepository jpaRepository;
  private final RouteEntityMapper routeEntityMapper;

  @PostConstruct
  public void init(){
    log.debug("Init {}", this.getClass().getSimpleName());
  }

  @Override
  public void save(RouteView routeView) {
    RouteEntity routeEntity = routeEntityMapper.toEntity(routeView);
    jpaRepository.save(routeEntity);
  }

  @Override
  public RouteView getById(String routeId) {
    RouteEntity routeEntity = jpaRepository.getReferenceById(routeId);
    return routeEntityMapper.toView(routeEntity);
  }

  @Override
  public List<RouteBaseView> fetchBaseRouteViews() {
    List<RouteEntity> routeEntities = jpaRepository.findAll();
    return routeEntityMapper.toBaseViews(routeEntities);
  }

  @Override
  public boolean isDriverAvailable(String driverId) {
    return !jpaRepository.existsByDriverIdAndStatus(driverId, STARTED_STATUS);
  }

}
