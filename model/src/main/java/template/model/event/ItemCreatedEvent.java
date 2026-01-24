package template.model.event;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import template.model.domain.Item;

@Data
@Builder
@Jacksonized
public class ItemCreatedEvent {

    private final String eventId;

    private final Item item;

}
