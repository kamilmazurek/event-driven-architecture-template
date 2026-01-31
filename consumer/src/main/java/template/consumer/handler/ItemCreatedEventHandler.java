package template.consumer.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import template.consumer.store.ItemStore;
import template.model.event.ItemCreatedEvent;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemCreatedEventHandler {

    private final ItemStore itemStore;

    public void handle(ItemCreatedEvent event) {
        var item = event.getItem();
        itemStore.add(item);
        log.info("Item added to item store: id={}, name={}", item.getId(), item.getName());
    }

}