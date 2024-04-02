package org.magellan.ddd.domain.application.repositories;

import org.magellan.ddd.domain.application.queries.ApplicationView;

public interface ApplicationRepository {

  ApplicationView getById(String applicationId);

  void save(ApplicationView applicationView);

}
