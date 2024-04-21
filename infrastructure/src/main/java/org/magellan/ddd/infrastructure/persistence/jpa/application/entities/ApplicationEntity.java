package org.magellan.ddd.infrastructure.persistence.jpa.application.entities;

import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "APPLICATION")
public class ApplicationEntity {

  @Id
  @Column(name = "ID")
  private String id;

  @Column(name = "STATUS")
  private String status;

  @Column(name = "DRIVER_ID")
  private String driverId;

  @Column(name = "VEHICLE_TYPE_ID")
  private Integer vehicleTypeId;

  @Column(name = "STARTED_DATE")
  private Instant startedDate;

  @Column(name = "CREATED_DATE")
  private Instant createdDate;

  @Column(name = "ROUTE_ID")
  private String routeId;
}
