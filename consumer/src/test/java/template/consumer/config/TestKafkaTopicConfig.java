package template.consumer.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import static template.model.topic.Topics.ITEM_CREATED;

@Configuration
public class TestKafkaTopicConfig {

    @Bean
    public NewTopic itemCreatedTopic() {
        return TopicBuilder.name(ITEM_CREATED).partitions(1).replicas(1).build();
    }

}
