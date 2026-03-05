package template.producer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import template.commons.model.domain.Item;
import template.commons.model.event.ItemCreatedEvent;
import template.producer.publisher.ItemEventPublisher;

import static java.util.UUID.randomUUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemEventPublisher publisher;

    public Item createItem(String name) {
        var item = Item.create(name);
        log.info("Created item with id {} and name {}", item.getId(), item.getName());

        var event = ItemCreatedEvent.builder().eventId(randomUUID().toString()).item(item).build();
        publisher.publish(event);

        return item;
    }

}
