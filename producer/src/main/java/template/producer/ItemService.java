package template.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import template.model.Item;
import template.model.ItemCreatedEvent;

import java.util.UUID;

import static java.util.UUID.randomUUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemEventPublisher publisher;

    public void createItem(Item item) {
        log.info("Creating item {}", item.getId());
        var event = ItemCreatedEvent.builder().eventId(randomUUID().toString()).item(item).build();
        publisher.publish(event);
    }

}
