package template.consumer.controller;


import org.junit.jupiter.api.Test;
import template.consumer.store.ItemStore;
import template.commons.model.domain.Item;

import java.util.List;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;

class ItemReadControllerTest {

    @Test
    void shouldReturnItemsFromStore() {
        //given items
        var itemA = Item.builder().id(randomUUID().toString()).name("Item A").build();
        var itemB = Item.builder().id(randomUUID().toString()).name("Item B").build();

        //and item store with items
        var itemStore = mock(ItemStore.class);
        when(itemStore.getAll()).thenReturn(List.of(itemA, itemB));

        //and controller
        var controller = new ItemReadController(itemStore);

        //when request is handled
        var response = controller.getItems();

        //then status is OK
        assertEquals(OK, response.getStatusCode());

        //and response contains correct DTOs
        var body = response.getBody();
        assertEquals(2, body.size());
        assertEquals(itemA.getId(), body.get(0).id());
        assertEquals(itemB.getId(), body.get(1).id());
    }
}