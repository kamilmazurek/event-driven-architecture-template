package template.consumer.store;

import org.springframework.stereotype.Component;
import template.commons.model.domain.Item;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class ItemStore {

    private final List<Item> items = new CopyOnWriteArrayList<>();

    public void add(Item item) {
        items.add(item);
    }

    public List<Item> getAll() {
        return List.copyOf(items);
    }

    public void clear() {
        items.clear();
    }

}