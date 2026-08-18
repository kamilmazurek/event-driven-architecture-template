package template.producer.publisher;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import template.commons.model.domain.Item;
import template.commons.model.event.ItemCreatedEvent;
import template.producer.AbstractProducerIT;

import java.time.Duration;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.kafka.test.utils.KafkaTestUtils.getSingleRecord;
import static template.commons.model.topic.Topics.ITEM_CREATED;

public class ItemEventPublisherIT extends AbstractProducerIT {

    @Autowired
    private ItemEventPublisher publisher;

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

}
