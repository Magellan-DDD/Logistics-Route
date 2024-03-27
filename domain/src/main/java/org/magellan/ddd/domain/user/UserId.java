package org.magellan.ddd.domain.user;

import org.magellan.ddd.domain.common.Identifiable;

public record UserId(String value) implements Identifiable<String> {

}
