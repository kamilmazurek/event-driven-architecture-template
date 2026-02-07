package template.consumer.listener;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.kafka.core.KafkaTemplate;
import template.consumer.AbstractConsumerIntegrationTest;
import template.commons.model.domain.Item;
import template.commons.model.event.ItemCreatedEvent;

import java.time.Duration;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static template.commons.model.topic.Topics.ITEM_CREATED;

@ExtendWith(OutputCaptureExtension.class)
public class ItemEventListenerIntegrationTest extends AbstractConsumerIntegrationTest {

    @Autowired
    private KafkaTemplate<String, ItemCreatedEvent> kafkaTemplate;

    @Test
    void shouldReceiveItemCreatedEvent(CapturedOutput output) {
        //given item
        var item = Item.builder().id(randomUUID().toString()).name("Item A").build();

        //and event
        var event = ItemCreatedEvent.builder().eventId(randomUUID().toString()).item(item).build();

        //when event is sent to Kafka
        kafkaTemplate.send(ITEM_CREATED, item.getId(), event);

        //then information about received event is logged
        await().atMost(Duration.ofSeconds(5)).untilAsserted(() ->
                assertThat(output.getOut()).contains("Received event " + event.getEventId())
        );
    }

}
