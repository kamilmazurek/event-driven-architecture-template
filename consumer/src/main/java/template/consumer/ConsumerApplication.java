package template.consumer;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.springframework.boot.SpringApplication.run;

@SpringBootApplication(scanBasePackages = {"template.consumer", "template.commons.health"})
public class ConsumerApplication {

    public static void main(String[] args) {
        run(ConsumerApplication.class, args);
    }

}
