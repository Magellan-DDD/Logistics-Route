package org.magellan.ddd.infrastructure.persistence.jpa.route;

import org.magellan.ddd.infrastructure.persistence.jpa.route.entities.RouteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteJpaRepository extends JpaRepository<RouteEntity, String> {

  boolean existsByDriverIdAndStatus(String driverId, String status);

}
