package template.consumer.handler;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import template.consumer.AbstractConsumerIT;
import template.commons.model.domain.Item;
import template.commons.model.dto.ItemDTO;
import template.commons.model.event.ItemCreatedEvent;

import java.time.Duration;
import java.util.Arrays;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static template.commons.model.topic.Topics.ITEM_CREATED;

public class ItemCreatedEventHandlerIT extends AbstractConsumerIT {

    @Autowired
    private KafkaTemplate<String, ItemCreatedEvent> kafkaTemplate;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldHandleItemCreatedEvent() {
        //given item
        var item = Item.builder().id(randomUUID().toString()).name("Item A").build();

        //and event
        var event = ItemCreatedEvent.builder().eventId(randomUUID().toString()).item(item).build();

        //when event is sent to Kafka
        kafkaTemplate.send(ITEM_CREATED, item.getId(), event);

        //then item is added to item store
        await().atMost(Duration.ofSeconds(5)).untilAsserted(() ->
                assertThat(itemStore.getAll()).extracting(Item::getId).contains(item.getId())
        );

        //and item can be obtained from items endpoint
        var response = restTemplate.getForEntity("/items", ItemDTO[].class);
        assertThat(Arrays.stream(response.getBody()).map(ItemDTO::id)).contains(item.getId());
    }

}