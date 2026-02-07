package template.commons.health;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static org.apache.kafka.clients.admin.AdminClient.create;
import static org.apache.kafka.clients.admin.AdminClientConfig.DEFAULT_API_TIMEOUT_MS_CONFIG;
import static org.apache.kafka.clients.admin.AdminClientConfig.REQUEST_TIMEOUT_MS_CONFIG;
import static org.apache.kafka.clients.admin.AdminClientConfig.RETRIES_CONFIG;
import static org.springframework.boot.actuate.health.Health.down;
import static org.springframework.boot.actuate.health.Health.up;


@Component("kafka")
@RequiredArgsConstructor
public class EdaKafkaHealthIndicator implements HealthIndicator {

    private final KafkaAdmin kafkaAdmin;

    @Override
    public Health health() {
        var config = new HashMap<>(kafkaAdmin.getConfigurationProperties());
        try (var client = create(withShortTimeout(config))) {
            var cluster = client.describeCluster();
            var clusterId = cluster.clusterId().get();
            return up().withDetail("clusterId", clusterId).build();
        } catch (Exception e) {
            return down(e).build();
        }
    }

    private Map<String, Object> withShortTimeout(Map<String, Object> config) {
        config.put(REQUEST_TIMEOUT_MS_CONFIG, 3000);
        config.put(DEFAULT_API_TIMEOUT_MS_CONFIG, 3000);
        config.put(RETRIES_CONFIG, 0);
        return config;
    }

}