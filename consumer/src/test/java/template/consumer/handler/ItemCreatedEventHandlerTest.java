package template.consumer.handler;

import org.junit.jupiter.api.Test;
import template.consumer.store.ItemStore;
import template.model.domain.Item;
import template.model.event.ItemCreatedEvent;

import static java.util.UUID.randomUUID;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class ItemCreatedEventHandlerTest {

    @Test
    void shouldHandleEvent() {
        //given store
        var itemStore = mock(ItemStore.class);

        //and handler
        var handler = new ItemCreatedEventHandler(itemStore);

        //and item
        var item = Item.builder().id(randomUUID().toString()).name("Item A").build();

        //and event
        var event = ItemCreatedEvent.builder().eventId(randomUUID().toString()).item(item).build();

        //when event is handled
        handler.handle(event);

        // then item is added to store
        verify(itemStore).add(item);
        verifyNoMoreInteractions(itemStore);
    }

}
