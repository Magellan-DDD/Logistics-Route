package org.magellan.ddd.domain.route;

public record Address(
    String zip,
    String region,
    String city,
    String street,
    String building) {
}
