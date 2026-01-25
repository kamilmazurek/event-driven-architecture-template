package template.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import lombok.SneakyThrows;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.HashMap;
import java.util.List;

import static org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_OFFSET_RESET_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static template.model.topic.Topics.ITEM_CREATED;

@Testcontainers
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = RANDOM_PORT)
public abstract class AbstractIT {

    @LocalServerPort
    private int port;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        RestAssured.port = this.port;
    }

    @DynamicPropertySource
    static void kafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", KafkaTestContainer::bootstrapServers);
    }

    protected <V> KafkaConsumer<String, V> createConsumer(String groupId, Class<V> valueType) {
        var props = new HashMap<String, Object>();
        props.put(BOOTSTRAP_SERVERS_CONFIG, KafkaTestContainer.bootstrapServers());
        props.put(GROUP_ID_CONFIG, groupId);
        props.put(AUTO_OFFSET_RESET_CONFIG, "earliest");

        var stringDeserializer = new StringDeserializer();
        var jsonDeserializer = new JsonDeserializer<>(valueType, objectMapper, false);

        return new KafkaConsumer<>(props, stringDeserializer, jsonDeserializer);
    }

    protected AdminClient createAdminClient() {
        var props = new HashMap<String, Object>();
        props.put(BOOTSTRAP_SERVERS_CONFIG, KafkaTestContainer.bootstrapServers());
        return AdminClient.create(props);
    }

    @AfterEach
    @SneakyThrows
    void cleanUpTopics() {
        try (var admin = createAdminClient()) {
            var topics = admin.listTopics().names().get();
            if (topics.contains(ITEM_CREATED)) {
                admin.deleteTopics(List.of(ITEM_CREATED)).all().get();
            }
        }
    }

}
