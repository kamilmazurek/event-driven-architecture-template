package template.consumer.listener;

import org.junit.jupiter.api.Test;
import template.consumer.handler.ItemCreatedEventHandler;
import template.model.domain.Item;
import template.model.event.ItemCreatedEvent;

import static java.util.UUID.randomUUID;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class ItemEventListenerTest {

    @Test
    void shouldDelegateEventToHandler() {
        //given handler
        var handler = mock(ItemCreatedEventHandler.class);

        //and listener
        var listener = new ItemEventListener(handler);

        //and event
        var item = Item.builder().id(randomUUID().toString()).name("Item A").build();
        var event = ItemCreatedEvent.builder().eventId(randomUUID().toString()).item(item).build();

        //when listener receives event
        listener.onItemCreated(event);

        //then event is handled by handler
        verify(handler).handle(event);
    }

}
