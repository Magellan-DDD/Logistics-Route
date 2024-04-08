package org.magellan.ddd.domain.route;

import jakarta.validation.constraints.NotBlank;

public record Address(
    String zip,
    @NotBlank
    String region,
    @NotBlank
    String city,
    @NotBlank
    String street,
    @NotBlank
    String building) {
}
