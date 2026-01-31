package template.producer.publisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import template.model.event.ItemCreatedEvent;

import static template.model.topic.Topics.ITEM_CREATED;

@Slf4j
@Component
@RequiredArgsConstructor
public class ItemEventPublisher {

    private final KafkaTemplate<String, ItemCreatedEvent> kafkaTemplate;

    public void publish(ItemCreatedEvent event) {
        var item = event.getItem();
        kafkaTemplate.send(ITEM_CREATED, item.getId(), event);
        log.info("Sent event {} informing about creating item with id {} and name {}", event.getEventId(), item.getId(), item.getName());
    }

}