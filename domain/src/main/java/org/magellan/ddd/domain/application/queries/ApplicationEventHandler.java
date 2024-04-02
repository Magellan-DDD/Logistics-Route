package org.magellan.ddd.domain.application.queries;

import lombok.RequiredArgsConstructor;
import org.axonframework.eventhandling.EventHandler;
import org.magellan.ddd.domain.application.events.ApplicationAcceptedEvent;
import org.magellan.ddd.domain.application.events.ApplicationSubmittedEvent;
import org.magellan.ddd.domain.application.mappers.ApplicationViewMapper;
import org.magellan.ddd.domain.application.repositories.ApplicationRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApplicationEventHandler {

  private final ApplicationRepository applicationRepository;
  private final ApplicationViewMapper applicationViewMapper;

  @EventHandler
  public void on(ApplicationSubmittedEvent event) {
    ApplicationView applicationView = applicationViewMapper.toView(event);
    applicationRepository.save(applicationView);
  }

  @EventHandler
  public void on(ApplicationAcceptedEvent event) {
    ApplicationView applicationView = applicationRepository.getById(event.applicationId().value());
    applicationView.setStatus(event.status().name());
    applicationRepository.save(applicationView);
  }

}
