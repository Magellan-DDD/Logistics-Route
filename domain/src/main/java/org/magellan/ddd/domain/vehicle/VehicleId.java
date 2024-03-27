package org.magellan.ddd.domain.vehicle;

import org.magellan.ddd.domain.common.Identifiable;

public record VehicleId(String value) implements Identifiable<String> {

}
