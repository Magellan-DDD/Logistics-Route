package org.magellan.ddd.infrastructure.persistence.jpa.route.entities;

import java.time.Instant;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.magellan.ddd.infrastructure.persistence.jpa.application.entities.ApplicationEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "ROUTE")
public class RouteEntity {

  @Id
  @Column(name = "ID")
  private String id;

  @Column(name = "STATUS")
  private String status;

  @Column(name = "DISPATCHER_ID")
  private String dispatcherId;

  @Column(name = "DRIVER_ID")
  private String driverId;

  @Column(name = "VEHICLE_ID")
  private String vehicleId;

  @Column(name = "STARTED_DATE")
  private Instant startedDate;

  @Column(name = "ACTUAL_ARRIVAL_DATE")
  private Instant actualArrivalDate;

  @Embedded
  private AddressEntity address;

  @Embedded
  private ScheduleEntity schedule;

  @OneToMany(targetEntity = ApplicationEntity.class,
      cascade = {CascadeType.REMOVE, CascadeType.DETACH}, orphanRemoval = true)
  @JoinColumn(name = "ROUTE_ID")
  private List<String> applicationIds;
}
