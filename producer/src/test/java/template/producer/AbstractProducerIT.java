package template.producer;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import template.model.event.ItemCreatedEvent;
import template.test.AbstractIT;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static template.model.topic.Topics.ITEM_CREATED;

public class AbstractProducerIT extends AbstractIT {

    protected KafkaConsumer<String, ItemCreatedEvent> consumer;

    @BeforeEach
    void setUp() throws ExecutionException, InterruptedException {
        createTopicIfNotExists(ITEM_CREATED);
        consumer = createConsumer("item-controller-it" + UUID.randomUUID(), ItemCreatedEvent.class);
        consumer.subscribe(List.of(ITEM_CREATED));
    }

    @AfterEach
    void cleanUp() throws ExecutionException, InterruptedException {
        consumer.close();
        deleteTopicIfExists(ITEM_CREATED);
    }

}
