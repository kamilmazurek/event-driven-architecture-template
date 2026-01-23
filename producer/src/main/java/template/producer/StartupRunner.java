package template.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import template.model.Item;

@Slf4j
@Component
@Profile("!test")
@RequiredArgsConstructor
public class StartupRunner implements CommandLineRunner {

    private final ItemService itemService;

    @Override
    public void run(String... args) {
        var item = Item.builder().id("1").build();
        itemService.createItem(item);
    }

}