package br.com.fullcycle.infrastructure.rest.presenters;

import br.com.fullcycle.application.Presenter;
import br.com.fullcycle.application.customer.GetCustomerByIdUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public class PublicGetCustomerByIdString implements Presenter<Optional<GetCustomerByIdUseCase.Output>, Object> {

    private static final Logger log = LoggerFactory.getLogger(PublicGetCustomerByIdString.class);

    @Override
    public String present(Optional<GetCustomerByIdUseCase.Output> output) {
        return output
                .map(it -> it.id())
                .orElseGet(() -> "not found");
    }

    @Override
    public String present(Throwable error) {
        log.error("An error was observed at GetCustomerByIdUseCase", error);
        return "not found";
    }
}
