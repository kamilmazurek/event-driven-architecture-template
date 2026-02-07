package template.producer;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.springframework.boot.SpringApplication.run;

@SpringBootApplication(scanBasePackages = {"template.producer", "template.commons.health"})
public class ProducerApplication {

    public static void main(String[] args) {
        run(ProducerApplication.class, args);
    }

}
