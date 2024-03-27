package org.magellan.ddd.domain.application;

import java.util.UUID;
import org.magellan.ddd.domain.common.Identifiable;

public record ApplicationId(String value) implements Identifiable<String> {

  public static ApplicationId newInstance() {
    return new ApplicationId(UUID.randomUUID().toString());
  }

}
