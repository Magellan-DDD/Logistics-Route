package org.magellan.ddd.presenation;

import org.magellan.ddd.domain.DomainApplication;
import org.magellan.ddd.infrastructure.InfrastructureApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackageClasses = {
    PresentationApplication.class,
    InfrastructureApplication.class,
    DomainApplication.class
})
public class PresentationApplication {

  public static void main(String[] args) {
    SpringApplication.run(PresentationApplication.class, args);
  }

}
