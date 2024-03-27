package org.magellan.ddd.domain.route;

import java.util.UUID;
import org.magellan.ddd.domain.common.Identifiable;

public record RouteId(String value) implements Identifiable<String> {

  public static RouteId newInstance() {
    return new RouteId(UUID.randomUUID().toString());
  }

}
