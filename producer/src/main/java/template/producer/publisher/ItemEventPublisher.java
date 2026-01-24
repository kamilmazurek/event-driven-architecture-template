package template.producer.publisher;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import template.model.event.ItemCreatedEvent;

import static template.model.topic.Topics.ITEM_CREATED;

@Component
@RequiredArgsConstructor
public class ItemEventPublisher {

    private final KafkaTemplate<String, ItemCreatedEvent> kafkaTemplate;

    public void publish(ItemCreatedEvent event) {
        kafkaTemplate.send(ITEM_CREATED, event.getItem().getId(), event);
    }

}