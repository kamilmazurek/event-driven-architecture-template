package template.commons.model.domain;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import static java.util.UUID.randomUUID;

@Data
@Builder
@Jacksonized
public class Item {

    private final String id;

    private final String name;

    public static Item create(String name) {
        return Item.builder().id(randomUUID().toString()).name(name).build();
    }

}
