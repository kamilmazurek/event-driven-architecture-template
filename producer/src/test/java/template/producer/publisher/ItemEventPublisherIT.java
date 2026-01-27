package template.producer.publisher;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import template.model.domain.Item;
import template.model.event.ItemCreatedEvent;
import template.test.AbstractIT;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.kafka.test.utils.KafkaTestUtils.getSingleRecord;
import static template.model.topic.Topics.ITEM_CREATED;

public class ItemEventPublisherIT extends AbstractIT {

    @Autowired
    private ItemEventPublisher publisher;

    private KafkaConsumer<String, ItemCreatedEvent> consumer;

    @BeforeEach
    void setUp() {
        consumer = createConsumer("item-event-publisher-it", ItemCreatedEvent.class);
        consumer.subscribe(List.of(ITEM_CREATED));
    }

    @Test
    void shouldPublishItemCreatedEventToKafka() {
        //given item
        var item = Item.builder().id(randomUUID().toString()).name("Item A").build();

        //and event
        var event = ItemCreatedEvent.builder().eventId(randomUUID().toString()).item(item).build();

        //when event is published
        publisher.publish(event);

        //then event has been sent to Kafka
        var record = getSingleRecord(consumer, ITEM_CREATED, Duration.ofSeconds(5));
        assertEquals(item.getId(), record.key());
        assertEquals(item.getName(), record.value().getItem().getName());
    }

    @AfterEach
    void cleanUp() throws ExecutionException, InterruptedException {
        consumer.close();
        deleteTopic(ITEM_CREATED);
    }

}
