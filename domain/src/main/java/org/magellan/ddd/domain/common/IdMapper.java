package org.magellan.ddd.domain.common;

import java.util.Optional;

public interface IdMapper {

  default String toStringId(Identifiable<String> id) {
    return getValueSafety(id);
  }

  default Integer toIntegerId(Identifiable<Integer> id) {
    return getValueSafety(id);
  }

  private <T> T getValueSafety(Identifiable<T> id) {
    return Optional.ofNullable(id)
        .map(Identifiable::value)
        .orElse(null);
  }

}
