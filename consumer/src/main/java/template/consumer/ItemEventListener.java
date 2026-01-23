package template.consumer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import template.model.ItemCreatedEvent;

import static template.model.Topics.ITEM_CREATED;

@Component
@RequiredArgsConstructor
public class ItemEventListener {

    private final ItemCreatedEventHandler handler;

    @KafkaListener(topics = ITEM_CREATED, groupId = "template-consumer")
    public void onItemCreated(ItemCreatedEvent event) {
        handler.handle(event);
    }

}
