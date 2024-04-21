package org.magellan.ddd.domain.driver;

import org.magellan.ddd.domain.common.Identifiable;

public record DriverId(String value) implements Identifiable<String> {

  @Override
  public String toString() {
    return value();
  }

}
