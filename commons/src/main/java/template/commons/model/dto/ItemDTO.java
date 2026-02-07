package template.commons.model.dto;

import jakarta.validation.constraints.NotBlank;
import template.commons.model.domain.Item;

public record ItemDTO(@NotBlank String id, @NotBlank String name) {

    public static ItemDTO fromItem(Item item) {
        return new ItemDTO(item.getId(), item.getName());
    }

}
