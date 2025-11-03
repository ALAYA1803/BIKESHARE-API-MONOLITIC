package com.bikeshare.api.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import java.time.Instant;
import java.util.Map;

@Component
public class AppHealthIndicator implements HealthIndicator {
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String HEALTH_SERVICE_URL = "http://health-service:8081/health";

    @Override
    public Health health() {
        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(HEALTH_SERVICE_URL, Map.class);
            if (response.getStatusCode().is2xxSuccessful() &&
                response.getBody() != null &&
                "UP".equals(response.getBody().get("status"))) {
                return Health.up()
                        .withDetail("app", "bikeshare-api")
                        .withDetail("health-service", "UP")
                        .withDetail("timestamp", Instant.now().toString())
                        .build();
            } else {
                return Health.down()
                        .withDetail("app", "bikeshare-api")
                        .withDetail("health-service", "DOWN")
                        .withDetail("timestamp", Instant.now().toString())
                        .build();
            }
        } catch (Exception e) {
            return Health.down()
                    .withDetail("app", "bikeshare-api")
                    .withDetail("health-service", "DOWN")
                    .withDetail("error", e.getMessage())
                    .withDetail("timestamp", Instant.now().toString())
                    .build();
        }
    }
}
