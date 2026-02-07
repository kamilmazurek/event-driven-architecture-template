package template.producer.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import template.commons.model.dto.ItemDTO;
import template.producer.dto.CreateItemDTO;
import template.producer.service.ItemService;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemCreateController {

    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<ItemDTO> createItem(@Valid @RequestBody CreateItemDTO createItemDTO) {
        var createdItem = itemService.createItem(createItemDTO);
        return ResponseEntity.status(CREATED).body(ItemDTO.fromItem(createdItem));
    }

}