package template.model;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class ItemCreatedEvent {

    private final String eventId;

    private final Item item;

}
