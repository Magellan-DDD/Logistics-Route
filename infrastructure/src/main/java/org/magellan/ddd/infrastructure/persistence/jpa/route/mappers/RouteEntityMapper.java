package org.magellan.ddd.infrastructure.persistence.jpa.route.mappers;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import java.util.List;
import org.magellan.ddd.domain.route.queries.RouteBaseView;
import org.magellan.ddd.domain.route.queries.RouteView;
import org.magellan.ddd.infrastructure.persistence.jpa.route.entities.RouteEntity;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(componentModel = SPRING)
public interface RouteEntityMapper {

  RouteEntity toEntity(RouteView routeView);

  RouteView toView(RouteEntity routeEntity);

  @Named("toBaseView")
  RouteBaseView toBaseView(RouteEntity routeEntity);

  @IterableMapping(qualifiedByName = "toBaseView")
  List<RouteBaseView> toBaseViews(List<RouteEntity> routeEntities);

}
