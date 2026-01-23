package template.producer.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import template.model.Item;
import template.producer.service.ItemService;
import template.producer.model.CreateItemDTO;
import template.producer.model.ItemDTO;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<ItemDTO> createItem(@Valid @RequestBody CreateItemDTO createItemDTO) {
        var createdItem = itemService.createItem(createItemDTO);
        return ResponseEntity.status(CREATED).body(toDTO(createdItem));
    }

    private ItemDTO toDTO(Item item) {
        return new ItemDTO(item.getId(), item.getName());
    }

}