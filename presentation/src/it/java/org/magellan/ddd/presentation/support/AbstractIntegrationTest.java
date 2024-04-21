package org.magellan.ddd.presentation.support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import java.util.function.Consumer;
import org.assertj.core.api.ObjectAssert;
import org.axonframework.messaging.unitofwork.DefaultUnitOfWork;
import org.axonframework.messaging.unitofwork.UnitOfWork;
import org.axonframework.modelling.command.Aggregate;
import org.axonframework.modelling.command.Repository;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

@ActiveProfiles("it")
@SpringBootTest(webEnvironment = RANDOM_PORT)
public abstract class AbstractIntegrationTest {

  @LocalServerPort
  protected int port;

  protected RequestSpecification buildRequestSpecification() {
    return RestAssured.given().port(port);
  }

  protected <T> void assertAgg(String routeId, Repository<T> repository, Consumer<ObjectAssert<T>> assertion) {
    UnitOfWork unitOfWork = DefaultUnitOfWork.startAndGet(null);
    try {
      Aggregate<T> aggregate = repository.load(routeId);
      aggregate.execute(route -> assertion.accept(assertThat(route)));
    } finally {
      unitOfWork.rollback();
    }
  }

}
