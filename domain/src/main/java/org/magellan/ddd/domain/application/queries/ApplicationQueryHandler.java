package org.magellan.ddd.domain.application.queries;

import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.magellan.ddd.domain.application.repositories.ApplicationRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApplicationQueryHandler {

  private final ApplicationRepository applicationRepository;

  @QueryHandler
  public ApplicationView handle(GetApplicationDetailsQuery query) {
    return applicationRepository.getById(query.applicationId());
  }

}
