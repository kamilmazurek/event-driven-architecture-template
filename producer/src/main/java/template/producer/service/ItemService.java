package template.producer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import template.model.Item;
import template.model.ItemCreatedEvent;
import template.producer.publisher.ItemEventPublisher;
import template.producer.model.CreateItemDTO;

import static java.util.UUID.randomUUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemEventPublisher publisher;

    public Item createItem(CreateItemDTO dto) {
        var item = Item.builder().id(randomUUID().toString()).name(dto.name()).build();
        log.info("Created item with id {} and name {}", item.getId(), item.getName());

        var event = ItemCreatedEvent.builder().eventId(randomUUID().toString()).item(item).build();
        publisher.publish(event);

        return item;
    }

}
