package org.yezproject.pet.gateway;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationGatewayFilterFactory extends AbstractGatewayFilterFactory<JwtAuthenticationGatewayFilterFactory.Config> {
    private final JwtHelper jwtHelper;

    public JwtAuthenticationGatewayFilterFactory(JwtHelper jwtHelper) {
        super(Config.class);
        this.jwtHelper = jwtHelper;
    }

    private Mono<Void> onError(final ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }

    private void addUserInfoToHeaders(ServerWebExchange exchange, String token) {
        JwtDetails jwtDetails = null;
        try {
            jwtDetails = jwtHelper.extractPayload(token);
        } catch (TokenExpiredException | TokenInvalidException e) {
            throw new RuntimeException(e);
        }
        ServerHttpRequest request = exchange.getRequest().mutate()
                .header("X-User-ID", jwtDetails.uid())
                .header("X-User-Mail", jwtDetails.subject())
                .header("X-User-Name", jwtDetails.name())
                .build();
        exchange.mutate().request(request).build();
    }

    private String extractToken(ServerWebExchange exchange) {
        String authHeader = exchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    private boolean isValidToken(String token) {
        return token != null && !isApiToken(token);
    }

    private boolean isApiToken(String bearerToken) {
        return bearerToken.startsWith("yzp_");
    }

    @Override
    public GatewayFilter apply(JwtAuthenticationGatewayFilterFactory.Config config) {
        return (exchange, chain) -> {
            String token = extractToken(exchange);
            if (isValidToken(token)) {
                addUserInfoToHeaders(exchange, token);
                return chain.filter(exchange);
            }
            return onError(exchange);
        };
    }

    public static class Config {
    }
}
