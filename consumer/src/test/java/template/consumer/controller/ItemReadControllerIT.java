package template.consumer.controller;


import org.junit.jupiter.api.Test;
import template.consumer.AbstractConsumerIT;
import template.commons.model.domain.Item;
import template.commons.model.dto.ItemDTO;

import static io.restassured.RestAssured.given;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;

public class ItemReadControllerIT extends AbstractConsumerIT {

    @Test
    void shouldReturnItemsFromStore() {
        //given items
        var itemA = Item.builder().id(randomUUID().toString()).name("Item A").build();
        var itemB = Item.builder().id(randomUUID().toString()).name("Item B").build();

        //and items are in store
        itemStore.add(itemA);
        itemStore.add(itemB);

        //when items are requested
        var items = given()
                .when()
                .get("/items")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList(".", ItemDTO.class);

        //then items are returned
        assertThat(items).extracting(ItemDTO::id).containsExactlyInAnyOrder(itemA.getId(), itemB.getId());
    }

}
