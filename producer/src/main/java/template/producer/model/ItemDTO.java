package template.producer.model;

import jakarta.validation.constraints.NotBlank;

public record ItemDTO(@NotBlank String id, @NotBlank String name) {

}
