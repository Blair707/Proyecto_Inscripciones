package com.educativa.gateway.config;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import reactor.core.publisher.Mono;

@Configuration
public class GatewayConfig {

    @Bean
    @Order(1)
    public GlobalFilter userContextFilter() {
        return (exchange, chain) ->
            exchange.getPrincipal()
                .filter(p -> p instanceof JwtAuthenticationToken)
                .cast(JwtAuthenticationToken.class)
                .flatMap(authToken -> {
                    Jwt jwt = authToken.getToken();
                    String sub = jwt.getSubject();
                    String email = jwt.getClaimAsString("emails");

                    var mutatedRequest = exchange.getRequest().mutate()
                        .header("X-User-Sub", sub != null ? sub : "");

                    if (email != null) {
                        mutatedRequest.header("X-User-Email", email);
                    }

                    return chain.filter(exchange.mutate()
                        .request(mutatedRequest.build())
                        .build());
                })
                .switchIfEmpty(chain.filter(exchange));
    }

    @Bean
    @Order(2)
    public GlobalFilter loggingFilter() {
        return (exchange, chain) -> chain.filter(exchange).then(Mono.fromRunnable(() -> {
            System.out.printf("[Gateway] %s %s -> %s%n",
                exchange.getRequest().getMethod(),
                exchange.getRequest().getPath(),
                exchange.getResponse().getStatusCode());
        }));
    }
}
