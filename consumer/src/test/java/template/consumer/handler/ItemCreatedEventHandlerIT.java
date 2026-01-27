package template.consumer.handler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.kafka.core.KafkaTemplate;
import template.model.domain.Item;
import template.model.event.ItemCreatedEvent;
import template.test.AbstractIT;

import java.time.Duration;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static template.model.topic.Topics.ITEM_CREATED;

@ExtendWith(OutputCaptureExtension.class)
public class ItemCreatedEventHandlerIT extends AbstractIT {

    @Autowired
    private KafkaTemplate<String, ItemCreatedEvent> kafkaTemplate;

    @Test
    void shouldHandleItemCreatedEvent(CapturedOutput output) {
        //given item
        var item = Item.builder().id(randomUUID().toString()).name("Item A").build();

        //and event
        var event = ItemCreatedEvent.builder().eventId(randomUUID().toString()).item(item).build();

        //when event is sent to Kafka
        kafkaTemplate.send(ITEM_CREATED, item.getId(), event);

        //then information about created item is logged
        await().atMost(Duration.ofSeconds(5)).untilAsserted(() ->
                assertThat(output.getOut()).contains("Created item with id " + item.getId() + " and name " + item.getName())
        );
    }

}