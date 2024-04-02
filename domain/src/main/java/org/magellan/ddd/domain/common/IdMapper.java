package org.magellan.ddd.domain.common;

public interface IdMapper {

  default String toStringId(Identifiable<String> id) {
    return id.value();
  }

  default Integer toIntegerId(Identifiable<Integer> id) {
    return id.value();
  }

}
