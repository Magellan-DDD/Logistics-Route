package org.magellan.ddd.infrastructure.persistence.jpa.application;

import org.magellan.ddd.infrastructure.persistence.jpa.application.entities.ApplicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationJpaRepository extends JpaRepository<ApplicationEntity, String> {
}
