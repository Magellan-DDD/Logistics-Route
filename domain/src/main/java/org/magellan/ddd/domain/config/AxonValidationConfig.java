package org.magellan.ddd.domain.config;

import java.util.Set;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.InterceptorChain;
import org.axonframework.messaging.MessageHandlerInterceptor;
import org.axonframework.messaging.unitofwork.UnitOfWork;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

@Configuration
public class AxonValidationConfig {


  @Bean
  public MessageHandlerInterceptor<CommandMessage<?>> commandValidationInterceptor(CommandBus commandBus) {
    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    MessageHandlerInterceptor<CommandMessage<?>> commandMessageMessageHandlerInterceptor =
        (UnitOfWork<? extends CommandMessage<?>> unitOfWork, InterceptorChain interceptorChain) -> {
          CommandMessage<?> command = unitOfWork.getMessage();
          Set<ConstraintViolation<Object>> violations = validator.validate(command.getPayload());

          if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
          }

          return interceptorChain.proceed();
        };
    commandBus.registerHandlerInterceptor(commandMessageMessageHandlerInterceptor);
    return commandMessageMessageHandlerInterceptor;
  }

}
