package template.producer.service;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import template.model.event.ItemCreatedEvent;
import template.producer.AbstractIT;
import template.producer.dto.CreateItemDTO;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.kafka.test.utils.KafkaTestUtils.getSingleRecord;
import static template.model.topic.Topics.ITEM_CREATED;

public class ItemServiceIT extends AbstractIT {

    @Autowired
    private ItemService itemService;

    private KafkaConsumer<String, ItemCreatedEvent> consumer;

    @BeforeEach
    void setUp() {
        consumer = createConsumer("item-service-it", ItemCreatedEvent.class);
        consumer.subscribe(List.of(ITEM_CREATED));
    }

    @Test
    void shouldCreateItemAndPublishEventToKafka() {
        //given DTO
        var dto = new CreateItemDTO("Item A");

        //when item is created
        var createdItem = itemService.createItem(dto);

        //then item with id is returned
        assertNotNull(createdItem.getId());
        assertEquals(dto.name(), createdItem.getName());

        //and event has been sent to Kafka
        var record = getSingleRecord(consumer, ITEM_CREATED);

        assertEquals(createdItem.getId(), record.key());
        assertEquals(createdItem.getName(), record.value().getItem().getName());
        assertNotNull(record.value().getEventId());
    }

    @AfterEach
    void cleanUp() {
        consumer.close();
    }

}
