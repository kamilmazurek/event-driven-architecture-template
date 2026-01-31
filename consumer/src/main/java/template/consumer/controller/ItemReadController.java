package template.consumer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import template.consumer.store.ItemStore;
import template.model.dto.ItemDTO;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemReadController {

    private final ItemStore itemStore;

    @GetMapping
    public ResponseEntity<List<ItemDTO>> getItems() {
        return ResponseEntity.ok(itemStore.getAll().stream().map(ItemDTO::fromItem).toList());
    }

}