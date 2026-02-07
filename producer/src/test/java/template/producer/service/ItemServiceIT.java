package template.producer.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import template.producer.AbstractProducerIT;
import template.producer.dto.CreateItemDTO;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.kafka.test.utils.KafkaTestUtils.getSingleRecord;
import static template.commons.model.topic.Topics.ITEM_CREATED;

public class ItemServiceIT extends AbstractProducerIT {

    @Autowired
    private ItemService itemService;

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
        var record = getSingleRecord(consumer, ITEM_CREATED, Duration.ofSeconds(5));

        assertEquals(createdItem.getId(), record.key());
        assertEquals(createdItem.getName(), record.value().getItem().getName());
        assertNotNull(record.value().getEventId());
    }

}
