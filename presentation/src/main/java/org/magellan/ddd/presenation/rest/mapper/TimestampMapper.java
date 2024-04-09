package org.magellan.ddd.presenation.rest.mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import java.time.Instant;
import org.mapstruct.Mapper;

@Mapper(componentModel = SPRING)
public interface TimestampMapper {

  default Long toTimestamp(Instant instant) {
    return instant.getEpochSecond();
  }

}
