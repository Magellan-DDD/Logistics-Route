package org.magellan.ddd.infrastructure.vehicle;

import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.magellan.ddd.domain.vehicle.accessors.VehicleAccessor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class VehicleCache implements VehicleAccessor {

  private static final Map<Integer, String> vehicleTypes = Map.of(
      1, "Car",
      2, "Bus",
      3, "Truck"
  );

  @Override
  public String getVehicleTypeName(Integer vehicleTypeId) {
    return Optional.ofNullable(vehicleTypes.get(vehicleTypeId))
        .orElseGet(() -> {
          log.warn("Vehicle type with id {} not found", vehicleTypeId);
          return "Unknown";
        });
  }

}
