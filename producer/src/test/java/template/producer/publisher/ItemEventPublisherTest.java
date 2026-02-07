package template.producer.publisher;

import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaTemplate;
import template.commons.model.domain.Item;
import template.commons.model.event.ItemCreatedEvent;

import static java.util.UUID.randomUUID;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static template.commons.model.topic.Topics.ITEM_CREATED;

class ItemEventPublisherTest {

    @Test
    @SuppressWarnings("unchecked")
    void shouldPublishItemCreatedEvent() {
        //given item
        var item = Item.builder().id(randomUUID().toString()).name("Item A").build();

        //and event
        var event = ItemCreatedEvent.builder().eventId(randomUUID().toString()).item(item).build();

        //and kafka template
        var kafkaTemplate = mock(KafkaTemplate.class);

        //and publisher
        var publisher = new ItemEventPublisher(kafkaTemplate);

        //when event is published
        publisher.publish(event);

        //then event is sent by kafka template
        verify(kafkaTemplate).send(ITEM_CREATED, item.getId(), event);
    }

}
