package template.producer.controller;

import org.junit.jupiter.api.Test;
import template.commons.model.domain.Item;
import template.producer.dto.CreateItemDTO;
import template.producer.service.ItemService;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.CREATED;

class ItemCreateControllerTest {

    @Test
    void shouldCreateItem() {
        //given DTO
        var dto = new CreateItemDTO("Item A");

        //and service
        var itemService = mock(ItemService.class);

        //and controller
        var controller = new ItemCreateController(itemService);

        //and service can create item based on DTO
        var item = Item.builder().id(randomUUID().toString()).name(dto.name()).build();
        when(itemService.createItem(dto)).thenReturn(item);

        //when request is handled
        var response = controller.createItem(dto);

        //then CREATED status is returned
        assertEquals(CREATED, response.getStatusCode());

        //and response body contains information about item
        var body = response.getBody();
        assertEquals(item.getId(), body.id());
        assertEquals(dto.name(), body.name());

        //and service was involved in creating item
        verify(itemService).createItem(dto);
    }

}
