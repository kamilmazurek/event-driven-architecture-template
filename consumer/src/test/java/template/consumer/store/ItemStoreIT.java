package template.consumer.store;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import template.consumer.AbstractConsumerIT;
import template.consumer.handler.ItemCreatedEventHandler;
import template.model.domain.Item;
import template.model.event.ItemCreatedEvent;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;

public class ItemStoreIT extends AbstractConsumerIT {

    @Autowired
    private ItemCreatedEventHandler handler;

    @Test
    void shouldReturnItems() {
        //given items
        var itemA = Item.builder().id(randomUUID().toString()).name("Item A").build();
        var itemB = Item.builder().id(randomUUID().toString()).name("Item B").build();

        //and events
        var eventA = ItemCreatedEvent.builder().eventId(randomUUID().toString()).item(itemA).build();
        var eventB = ItemCreatedEvent.builder().eventId(randomUUID().toString()).item(itemB).build();

        //and events have been handled
        handler.handle(eventA);
        handler.handle(eventB);

        //when items are requested
        var items = itemStore.getAll();

        //then items are returned
        assertThat(items).containsExactlyInAnyOrder(itemA, itemB);
    }

    @Test
    void shouldClearStore() {
        //given item
        var item = Item.builder().id(randomUUID().toString()).name("Item").build();

        //and event
        var event = ItemCreatedEvent.builder().eventId(randomUUID().toString()).item(item).build();

        //and event with item has been processed
        handler.handle(event);

        //and store contains item
        assertThat(itemStore.getAll()).containsExactly(item);

        //when store is cleared
        itemStore.clear();

        //then store no longer contains item
        assertThat(itemStore.getAll()).isEmpty();
    }

}
