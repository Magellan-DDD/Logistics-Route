package org.magellan.ddd.domain.route;

import java.time.Instant;

public record Schedule(Instant departureDate, Instant arrivalDate) {
}
