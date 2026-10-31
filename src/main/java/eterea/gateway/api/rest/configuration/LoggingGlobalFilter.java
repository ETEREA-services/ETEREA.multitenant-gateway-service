package eterea.gateway.api.rest.configuration;

import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class LoggingGlobalFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, @NonNull GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        long startTime = System.currentTimeMillis();

        // Log incoming request
        log.info("Incoming request: {} {} from {}",
            request.getMethod(),
            request.getURI(),
            request.getRemoteAddress() != null ? request.getRemoteAddress().getAddress().getHostAddress() : "unknown");

        // Log request headers, omitiendo credenciales
        request.getHeaders().forEach((key, values) -> {
            if (esHeaderSensible(key)) {
                log.debug("Request header: {} = [REDACTED]", key);
            } else {
                log.debug("Request header: {} = {}", key, values);
            }
        });

        // Log the route if available
        Object routeObj = exchange.getAttribute("org.springframework.cloud.gateway.support.ServerWebExchangeUtils.gatewayRoute");
        if (routeObj instanceof Route route) {
            log.info("Routing to: {} -> {}", request.getURI(), route.getUri());
        }

        return chain.filter(exchange)
            .doOnSuccess(aVoid -> {
                ServerHttpResponse response = exchange.getResponse();
                long duration = System.currentTimeMillis() - startTime;

                // Log response
                log.info("Response: {} {} in {}ms",
                    response.getStatusCode(),
                    exchange.getRequest().getURI(),
                    duration);

                // Log response headers, omitiendo credenciales
                response.getHeaders().forEach((key, values) -> {
                    if (esHeaderSensible(key)) {
                        log.debug("Response header: {} = [REDACTED]", key);
                    } else {
                        log.debug("Response header: {} = {}", key, values);
                    }
                });
            })
            .doOnError(throwable -> {
                long duration = System.currentTimeMillis() - startTime;
                log.error("Error processing request: {} {} in {}ms - Error: {}",
                    request.getMethod(),
                    request.getURI(),
                    duration,
                    throwable.getMessage(),
                    throwable);
            });
    }

    private boolean esHeaderSensible(String key) {
        String header = key.toLowerCase(java.util.Locale.ROOT);
        return "authorization".equals(header)
                || "cookie".equals(header)
                || "set-cookie".equals(header)
                || "proxy-authorization".equals(header);
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE; // Execute after other filters
    }
}