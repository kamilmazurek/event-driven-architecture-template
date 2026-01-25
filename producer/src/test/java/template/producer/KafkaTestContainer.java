package template.producer;

import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

public final class KafkaTestContainer {

    private KafkaTestContainer() {
        //shared container
    }

    private static final KafkaContainer KAFKA = new KafkaContainer(DockerImageName.parse("apache/kafka:3.9.1"));

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(KAFKA::stop));
    }

    public static String bootstrapServers() {
        if (!KAFKA.isRunning()) {
            KAFKA.start();
        }

        return KAFKA.getBootstrapServers();
    }

}
