package org.magellan.ddd.infrastructure.persistence.jpa.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.magellan.ddd.domain.application.queries.ApplicationView;
import org.magellan.ddd.domain.application.repositories.ApplicationRepository;
import org.magellan.ddd.infrastructure.persistence.jpa.application.entities.ApplicationEntity;
import org.magellan.ddd.infrastructure.persistence.jpa.application.mapper.ApplicationEntityMapper;
import org.springframework.stereotype.Repository;
import jakarta.annotation.PostConstruct;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ApplicationDAO implements ApplicationRepository {

  private final ApplicationJpaRepository jpaRepository;
  private final ApplicationEntityMapper entityMapper;

  @PostConstruct
  public void init() {
    log.debug("Init {}", this.getClass().getSimpleName());
  }

  @Override
  public ApplicationView getById(String applicationId) {
    ApplicationEntity applicationEntity = jpaRepository.getReferenceById(applicationId);
    return entityMapper.toView(applicationEntity);
  }

  @Override
  public void save(ApplicationView applicationView) {
    ApplicationEntity applicationEntity = entityMapper.toEntity(applicationView);
    jpaRepository.save(applicationEntity);
  }
}
