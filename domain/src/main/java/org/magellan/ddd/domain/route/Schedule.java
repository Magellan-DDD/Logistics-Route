package org.magellan.ddd.domain.route;

import java.time.Instant;
import jakarta.validation.constraints.NotBlank;

public record Schedule(
    @NotBlank
    Instant departureDate,
    @NotBlank
    Instant arrivalDate) {
}
