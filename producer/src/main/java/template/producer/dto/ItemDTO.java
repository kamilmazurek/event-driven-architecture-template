package template.producer.dto;

import jakarta.validation.constraints.NotBlank;

public record ItemDTO(@NotBlank String id, @NotBlank String name) {

}
