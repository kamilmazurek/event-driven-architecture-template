package template.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import template.model.Item;
import template.model.ItemCreatedEvent;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ItemEventPublisher {

    private static final String ITEM_CREATED_TOPIC = "item-created";

    private final KafkaTemplate<String, ItemCreatedEvent> kafkaTemplate;

    public void publishItemCreated(Item item) {
        var event = ItemCreatedEvent.builder().eventId(UUID.randomUUID().toString()).item(item).build();
        kafkaTemplate.send(ITEM_CREATED_TOPIC, item.getId(), event);
    }
}