package org.magellan.ddd.domain.driver.events;

import org.magellan.ddd.domain.driver.Driver;

public record DriverStatusChangedEvent(Driver driver) {
}
