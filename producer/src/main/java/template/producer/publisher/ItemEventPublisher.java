package template.producer.publisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import template.commons.model.event.ItemCreatedEvent;

import static template.commons.model.topic.Topics.ITEM_CREATED;

@Slf4j
@Component
@RequiredArgsConstructor
public class ItemEventPublisher {

    private final KafkaTemplate<String, ItemCreatedEvent> kafkaTemplate;

    public void publish(ItemCreatedEvent event) {
        var item = event.getItem();
        kafkaTemplate.send(ITEM_CREATED, item.getId(), event);
        log.info("Published ItemCreatedEvent {} for item id {} and name {}", event.getEventId(), item.getId(), item.getName());
    }

}