package template.consumer.handler;

import org.junit.jupiter.api.Test;
import template.model.domain.Item;
import template.model.event.ItemCreatedEvent;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class ItemCreatedEventHandlerTest {

    @Test
    void shouldHandleEvent() {
        //given handler
        var handler = new ItemCreatedEventHandler();

        //and item
        var item = Item.builder().id(randomUUID().toString()).name("Item A").build();

        //and event
        var event = ItemCreatedEvent.builder().eventId(randomUUID().toString()).item(item).build();

        //when event is handled then no exception is thrown
        assertDoesNotThrow(() -> handler.handle(event));
    }

}
