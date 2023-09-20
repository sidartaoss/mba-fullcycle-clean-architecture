package br.com.fullcycle.infrastructure.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.function.HandlerFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.Optional;

public class SpringHttpRouter implements HttpRouter {

    private final static Logger log = LoggerFactory.getLogger(SpringHttpRouter.class);

    private final RouterFunctions.Builder router;

    public SpringHttpRouter() {
        this.router = RouterFunctions.route();
    }

    public RouterFunctions.Builder router() {
        return router;
    }

    @Override
    public <T> HttpRouter POST(String pattern, HttpHandler<T> aHandler) {
        this.router.POST(pattern, wrapHandler(pattern, aHandler));
        return this;
    }

    @Override
    public <T> HttpRouter GET(String pattern, HttpHandler<T> aHandler) {
        this.router.GET(pattern, wrapHandler(pattern, aHandler));
        return this;
    }

    public record SpringHttpRequest(
            ServerRequest request
    ) implements HttpRequest {

        @Override
        public <T> T body(final Class<T> tClass) {
            try {
                return request.body(tClass);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public String pathParam(String name) {
            return request.pathVariable(name);
        }

        @Override
        public Optional<String> queryParam(String name) {
            return request.param(name);
        }

    }

    private static <T> HandlerFunction<ServerResponse> wrapHandler(String pattern, HttpHandler<T> aHandler) {
        return request -> {
            try {
                final var response = aHandler.handle(new SpringHttpRequest(request));
                return ServerResponse.status(response.statusCode())
                        .headers(headers -> response.headers().forEach(headers::add))
                        .body(response.body());
            } catch (final Throwable t) {
                log.error("Unexpected error was observed at %s".formatted(pattern), t);
                return ServerResponse.status(500).body("Unexpected error was observed");
            }
        };
    }
}
