package template.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import template.model.ItemCreatedEvent;

@Slf4j
@Service
public class ItemCreatedEventHandler {

    public void handle(ItemCreatedEvent event) {
        log.info("Item {} created", event.getItem().getId());
    }

}
