package template.consumer;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import template.consumer.store.ItemStore;
import template.test.AbstractIntegrationTest;

public abstract class AbstractConsumerIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    protected ItemStore itemStore;

    @AfterEach
    void cleanUp() {
        itemStore.clear();
    }

}
