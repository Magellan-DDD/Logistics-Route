package org.magellan.ddd.domain.dispatcher;

import org.magellan.ddd.domain.common.Identifiable;

public record DispatcherId(String value) implements Identifiable<String> {

  @Override
  public String toString() {
    return value();
  }

}
