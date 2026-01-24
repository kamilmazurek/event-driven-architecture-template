package template.producer.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateItemDTO(@NotBlank String name) {

}