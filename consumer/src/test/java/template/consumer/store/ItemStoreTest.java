package template.consumer.store;

import org.junit.jupiter.api.Test;
import template.commons.model.domain.Item;

import java.util.List;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ItemStoreTest {

    @Test
    void shouldAddAndReturnItems() {
        //given store
        var store = new ItemStore();

        //and items
        var itemA = Item.builder().id(randomUUID().toString()).name("Item A").build();
        var itemB = Item.builder().id(randomUUID().toString()).name("Item B").build();

        //when items are added
        store.add(itemA);
        store.add(itemB);

        //then items can be retrieved from store
        var items = store.getAll();
        assertThat(items).containsExactlyInAnyOrder(itemA, itemB);
    }

    @Test
    void shouldReturnImmutableListFromFindAll() {
        //given store with one item
        var store = new ItemStore();
        var item = Item.builder().id(randomUUID().toString()).name("Item A").build();
        store.add(item);

        //when list is fetched
        List<Item> items = store.getAll();

        //then list is immutable
        assertThat(items).isNotNull();
        assertThatThrownBy(() -> items.add(item)).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void shouldClearStore() {
        //given store with item
        var store = new ItemStore();
        store.add(Item.builder().id(randomUUID().toString()).name("Item A").build());

        //when store is cleared
        store.clear();

        //then store no longer contains item
        assertThat(store.getAll()).isEmpty();
    }
}
