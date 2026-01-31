package template.producer.controller;

import org.junit.jupiter.api.Test;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import template.producer.AbstractProducerIT;
import template.producer.dto.CreateItemDTO;

import java.time.Duration;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.HttpStatus.CREATED;
import static template.model.topic.Topics.ITEM_CREATED;

public class ItemCreateControllerIT extends AbstractProducerIT {

    @Test
    void shouldCreateItemAndPublishEvent() {
        //given DTO
        var dto = new CreateItemDTO("Item A");

        //when POST request with DTO is sent
        var itemId = given()
                .contentType(JSON)
                .body(dto)
                .when()
                .post("/items")
                .then()
                .statusCode(CREATED.value())
                .body("name", equalTo(dto.name()))
                .extract()
                .path("id");

        //then item id is set
        assertNotNull(itemId);

        //and event has been sent to Kafka
        var record = KafkaTestUtils.getSingleRecord(consumer, ITEM_CREATED, Duration.ofSeconds(5));
        var event = record.value();

        assertEquals(itemId, event.getItem().getId());
        assertEquals(dto.name(), event.getItem().getName());
    }

}
