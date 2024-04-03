package org.magellan.ddd.infrastructure.persistence.jpa.route.entities;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Getter
@Setter
@Embeddable
public class AddressEntity {

  @Column(name = "ZIP")
  private String zip;

  @Column(name = "REGION")
  private String region;

  @Column(name = "CITY")
  private String city;

  @Column(name = "STREET")
  private String street;

  @Column(name = "BUILDING")
  private String building;
}
