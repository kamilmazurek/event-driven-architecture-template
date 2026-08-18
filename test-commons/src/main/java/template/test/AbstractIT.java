package template.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
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
import java.util.concurrent.ExecutionException;

import static org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_OFFSET_RESET_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@Testcontainers
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = RANDOM_PORT)
public abstract class AbstractIT {

    @LocalServerPort
    private int port;

    @Autowired
    private ObjectMapper objectMapper;

    private static AdminClient adminClient;

    @BeforeAll
    static void initAdminClient() {
        var props = new HashMap<String, Object>();
        props.put(BOOTSTRAP_SERVERS_CONFIG, KafkaTestContainer.bootstrapServers());
        adminClient = AdminClient.create(props);
    }

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

    protected boolean topicExists(String topicName) throws ExecutionException, InterruptedException {
        return adminClient.listTopics().names().get().contains(topicName);
    }

    protected void createTopicIfNotExists(String topicName) throws ExecutionException, InterruptedException {
        if (!topicExists(topicName)) {
            adminClient.createTopics(List.of(new NewTopic(topicName, 1, (short) 1))).all().get();
        }
    }

    protected void deleteTopic(String topicName) throws ExecutionException, InterruptedException {
        deleteTopics(List.of(topicName));
    }

    protected void deleteTopics(List<String> topicNames) throws ExecutionException, InterruptedException {
        adminClient.deleteTopics(topicNames).all().get();
    }

    protected void deleteTopicIfExists(String topicName) throws ExecutionException, InterruptedException {
        if (topicExists(topicName)) {
            deleteTopic(topicName);
        }
    }

    @AfterAll
    static void closeAdminClient() {
        if (adminClient != null) {
            adminClient.close();
        }
    }

}
