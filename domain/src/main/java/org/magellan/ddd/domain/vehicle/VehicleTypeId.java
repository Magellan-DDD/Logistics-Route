package org.magellan.ddd.domain.vehicle;

import org.magellan.ddd.domain.common.Identifiable;

public record VehicleTypeId(Integer value) implements Identifiable<Integer> {
}
