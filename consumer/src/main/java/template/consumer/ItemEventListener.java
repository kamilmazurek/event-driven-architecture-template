package template.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import template.model.ItemCreatedEvent;

@Slf4j
@Component
@RequiredArgsConstructor
public class ItemEventListener {

    @KafkaListener(topics = "item-created", groupId = "template-consumer")
    public void onItemCreated(ItemCreatedEvent event) {
        log.info("Item {} created", event.getItem().getId());
    }

}
