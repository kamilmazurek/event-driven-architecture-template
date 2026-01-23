package template.producer.model;

import jakarta.validation.constraints.NotBlank;

public record CreateItemDTO(@NotBlank String name) {

}