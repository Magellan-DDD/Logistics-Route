package org.magellan.ddd.infrastructure.persistence.jpa.route.mappers;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import java.util.List;
import org.magellan.ddd.domain.route.queries.RouteBaseView;
import org.magellan.ddd.domain.route.queries.RouteView;
import org.magellan.ddd.infrastructure.persistence.jpa.application.entities.ApplicationEntity;
import org.magellan.ddd.infrastructure.persistence.jpa.route.entities.RouteEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.util.CollectionUtils;

@Mapper(componentModel = SPRING)
public interface RouteEntityMapper {

  @Mapping(target = "applications", source = "applicationIds", qualifiedByName = "toApplicationEntity")
  RouteEntity toEntity(RouteView routeView);

  RouteView toView(RouteEntity routeEntity);

  @Named("toBaseView")
  RouteBaseView toBaseView(RouteEntity routeEntity);

  @IterableMapping(qualifiedByName = "toBaseView")
  List<RouteBaseView> toBaseViews(List<RouteEntity> routeEntities);

  @AfterMapping
  default void populateRoutId(@MappingTarget RouteEntity routeEntity) {

    String routeId = routeEntity.getId();
    List<ApplicationEntity> applications = routeEntity.getApplications();
    if (!CollectionUtils.isEmpty(applications)) {
      applications.forEach(application -> application.setRouteId(routeId));
    }
  }

  @Named("toApplicationEntity")
  @Mapping(target = "id", source = "applicationId")
  ApplicationEntity toApplicationEntity(String applicationId);

}
