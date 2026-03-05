package template.producer.service;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import template.commons.model.event.ItemCreatedEvent;
import template.producer.dto.CreateItemDTO;
import template.producer.publisher.ItemEventPublisher;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class ItemServiceTest {

    @Test
    void shouldCreateItemAndPublishEvent() {
        //given DTO
        var dto = new CreateItemDTO("Item A");

        //and publisher
        var publisher = mock(ItemEventPublisher.class);

        //and service
        var service = new ItemService(publisher);

        //when item is created
        var item = service.createItem(dto.name());

        //then item is returned
        assertEquals(dto.name(), item.getName());

        //and item id has been set
        assertNotNull(item.getId());

        //and event is published
        var eventCaptor = ArgumentCaptor.forClass(ItemCreatedEvent.class);
        verify(publisher).publish(eventCaptor.capture());

        //and event contains created item
        var event = eventCaptor.getValue();
        assertEquals(item.getId(), event.getItem().getId());
        assertEquals(item.getName(), event.getItem().getName());

        //and event id has been set
        assertNotNull(event.getEventId());
    }

}
