> This documentation is also available in an enhanced form at
> [Event-Driven Architecture Template](https://kamilmazurek.pl/event-driven-architecture-template) page.

# Event-Driven Architecture Template

This repository contains the implementation of a Java-based microservice template that follows the principles of Event-Driven Architecture (EDA).
Built with Spring Boot and powered by Apache Kafka, the template provides a clean foundation for building scalable and resilient distributed applications, which communicate through events rather than direct service-to-service calls.
With a modular structure and clear separation between domain logic, messaging infrastructure, and configuration, it serves as a reusable starting point for modern microservice ecosystems.
Key advantages:
* **Loose Coupling:** Services communicate through events, reducing direct dependencies and enabling independent evolution.
* **Asynchronous Communication:** Non-blocking interactions improve responsiveness and overall system resilience.
* **Scalability:** Producers and consumers can scale independently based on workload and traffic patterns.
* **Resilience:** Event-driven flows help isolate failures and prevent cascading outages across services.
* **Extensibility:** New consumers can subscribe to existing events without modifying the producing service.
* **Event Replay and Traceability:** Kafka's log-based model enables event replay and improves observability of system behavior.
* **Developer Productivity:** A ready-to-use structure accelerates the development of new event-driven microservices while maintaining consistency.

The goal is to keep the template practical, clean, and easy to adapt, providing a solid foundation for building event-driven microservices.

## Quickstart

Following steps provide a quick way to get started with the Event-Driven Architecture Template:
1. Ensure a JDK is available to build and run the code. Temurin, based on OpenJDK and available from [adoptium.net](https://adoptium.net/), can be used for this purpose.
2. The project uses Docker to run Kafka. It is possible to run the template without it, but using Docker is recommended to get started quickly. The following steps assume Docker is installed, so please make sure you have it before proceeding.
3. Download the source code either by cloning the repository with Git or by downloading the ZIP file. If you downloaded the ZIP, extract it. Then navigate to the project folder.
4. Build the application and start it using Docker Compose:
    ```shell
    mvnw clean package
    docker compose up --build
    ```
    Rebuilds and deployments may accumulate unused Docker images and volumes, consuming additional disk space over time.
    It is possible to clean up previous images and volumes when rebuilding the application:
    ```shell
    mvnw clean package
    docker compose down --rmi local --volumes && docker compose up --build
    ```   
5. Verify that the application is running by sending a POST request to the Producer service:
    ```shell
    curl -i -X POST http://localhost:8080/items -H "Content-Type: application/json" -d '{"name":"Item A"}'
    ```
6. Check the Consumer by opening:
    ```console
    http://localhost:8081/items
    ```
   The response should contain an item like:
    ```json
    [
      {
        "id": "bc0ac641-b5f0-4e99-b067-926cead738f9",
        "name": "Item A"
      }
    ]
    ```
7. Customize the source code as needed, rebuild the project, and run the application 🚀.

## Table of Contents
* [Motivation](#motivation)
* [Architecture Overview](#architecture-overview)
* [Apache Kafka as the Event Backbone](#apache-kafka-as-the-event-backbone)
* [When to Use Event-Driven Architecture](#when-to-use-event-driven-architecture) 
* [Technology Stack](#technology-stack)
* [How It Works](#how-it-works)
* [Build and Deployment](#build-and-deployment)
* [Events, Topics, and Endpoints](#events-topics-and-endpoints)
* [Production-Ready Features](#production-ready-features)
* [Tests](#tests)
* [Additional Resources](#additional-resources)
* [Author](#author)
* [Disclaimer](#disclaimer)

## Motivation

Starting new event-driven microservices often involves repetitive setup.
I wanted the ability to start quickly and considered creating a Maven Archetype to automate this, but it felt too costly compared to the benefits.
I also tried AI/LLM-based solutions for generating project scaffolding, but they lacked determinism and sometimes produced unreliable results.
Instead, to reduce the overhead of repeatedly creating project skeletons, I decided to create this template.

## Architecture Overview

Event-Driven Architecture (EDA) is a software design pattern in which components communicate asynchronously by producing and consuming events, rather than invoking each other directly.
This approach allows services to operate independently, promotes loose coupling, improves scalability, and makes it easier to extend or modify individual components without impacting the rest of the system.
EDA is particularly well-suited for distributed microservices, where responsiveness, resilience, and flexibility are critical.

In this implementation, the architecture is centered around Kafka as the communication backbone.
The image below illustrates the concept used in this project:

**TODO: add image**

The main parts of this template include:
* **Producer**
    * Publishes domain events to Kafka topic when business actions occur.
    * Converts internal domain changes into events that other services can consume.
* **Consumer**
    * Subscribes to the Kafka topic and processes events asynchronously.
    * Encapsulates business logic triggered by incoming events while remaining decoupled from the producer.
* **Model**
    * Defines the event payloads and domain objects shared between producer and consumer.
    * Ensures consistent data structure and serialization across services.
* **Broker (Apache Kafka)**
    * Serves as the messaging backbone and distributed event log, storing events as an ordered sequence.
    * Provides partitioned storage with configurable durability (e.g., replication, acknowledgments).
    * Enables event replay, retention policies, and scalable consumption by multiple consumers.
    * Supports decoupled communication between producers and consumers.
* **Supporting Components**
    * Spring Boot Actuator for health checks.

This template focuses on simplicity and clarity, providing a solid starting point for building event-driven microservices.
While it currently supports a single producer and consumer, the structure is designed to be easily extended with additional services, topics, or event flows as your system grows.

By following Event-Driven Architecture principles, the design ensures asynchronous communication, loose coupling, and clear separation of concerns, helping developers create scalable, resilient, and maintainable microservices.

## Apache Kafka as the Event Backbone

Apache Kafka is a distributed event-streaming platform commonly used as a message broker in event-driven architectures.
Instead of directly coupling services through synchronous calls, Kafka allows services to communicate asynchronously by publishing and consuming events.
This decoupling improves scalability, reliability, and flexibility in modern microservice systems.

At its core, Kafka works as a distributed commit log where records are written sequentially and stored for a configurable period of time.
Each record represents a message containing a key, value, timestamp, and metadata describing its position in the log.

In event-driven systems, these records typically carry event data describing something that happened in the domain.
For example, a record might represent an `ItemCreated` event produced when a new item is created in the system.
Services acting as producers publish events to Kafka topics, while consumers subscribe to those topics and process them independently.

As a result, the key components include:
* **Producer** - a service that publishes events to a Kafka topic
* **Consumer** - a service that subscribes to a topic and processes incoming events
* **Broker** - a Kafka server that receives events from producers, stores them, and serves them to consumers
* **Topic** - a categorized stream of events
* **Partition** - a subdivision of a topic, important for parallel processing and scalability

Apache Kafka is designed for high throughput and distributed scalability.  
Additionally, events written to Kafka are persisted to disk and may be replicated across brokers, to improve durability and fault tolerance.  
These capabilities make Kafka particularly well suited for event-driven architectures:
* **Durable event storage** - records are persisted and can be replayed by consumers
* **Horizontal scalability** - topics can be partitioned across multiple brokers
* **High throughput** - designed to handle millions of records efficiently
* **Loose coupling** - producers and consumers can evolve independently
* **Event replay** - consumers can reprocess events by resetting offsets

With these features, Kafka can be used not only as a broker, but also as an event backbone for microservice ecosystems.

## When to Use Event-Driven Architecture

Event-Driven Architecture, also known as EDA, organizes a system around the production, detection, and reaction to events.
Instead of tightly coupling components through direct calls, different parts of the system communicate by emitting and consuming events.
This approach promotes loose coupling, scalability, and responsiveness, making it easier to evolve and extend your application over time.

This architecture is particularly effective for systems that must handle asynchronous workflows or high volumes of distributed interactions.
It works well in environments where multiple services or modules need to react to changes independently, such as processing orders, updating analytics, sending notifications, or integrating with external systems.
It may also be a strong fit for systems that require high scalability and resilience.
Additionally, by decoupling producers from consumers, teams can develop, deploy, and scale components independently, which improves flexibility and supports continuous delivery.

However, an Event-Driven Architecture introduces additional complexity.
Managing event consistency, tracing flows across services, handling failures, and ensuring reliable delivery requires careful design and tooling.
For smaller applications, systems with simple request–response flows, or projects with tight deadlines, this added complexity may outweigh the benefits.
In such cases, a [Hexagonal Architecture](https://kamilmazurek.pl/hexagonal-architecture-template) can provide a more straightforward and maintainable approach,
while a [Layered Architecture](https://kamilmazurek.pl/layered-architecture-template) may be suitable if an even simpler structure is desired.

Ultimately, choose Event-Driven Architecture when your system must be highly scalable, reactive, and loosely coupled.
When implemented thoughtfully, it enables resilient, extensible systems that can evolve naturally as new consumers, integrations, and business capabilities emerge.

## Technology Stack

Event-Driven Architecture Template is built using Java and Spring Boot, providing a solid foundation for developing modular and production-ready microservices.
The application follows an event-driven approach where services communicate asynchronously through a message broker rather than direct calls. This ensures loose coupling and independent scalability of components.

Apache Kafka is used as the central event streaming platform, enabling reliable, high throughput communication between producers and consumers.
Kafka allows services to publish events and react to them independently, which aligns naturally with the principles of Event-Driven Architecture.
Spring for Apache Kafka simplifies integration, configuration, and message handling within the Spring ecosystem.

Testing is a first-class concern in this stack. Unit tests focus on individual components such as event producers, consumers, and domain services, while integration tests verify message flow and interaction with Kafka.
Maven Surefire and Failsafe plugins are preconfigured to ensure smooth execution of both unit and integration tests during the build process.

Here is an overview of the technology stack:
- **Language & Framework**
  - **Java 21**: Modern Java version used to implement the application.
  - **Spring Boot**: Framework for building standalone, production-ready Spring applications.
  - **Spring for Apache Kafka**: Simplifies Kafka integration and event listener configuration.

- **Messaging**
  - **Apache Kafka**: Distributed event streaming platform enabling asynchronous communication between services.

- **Testing**
  - **JUnit**: Framework for writing unit tests in Java.
  - **REST Assured**: Library for testing REST endpoints.
  - **Mockito**: Mocking framework for isolating components during unit tests.
  - **Testcontainers**: Runs real infrastructure dependencies (e.g. Apache Kafka) in Docker containers for reliable integration testing.
  - **Allure Report**: Generates detailed and readable test reports.

- **Build & Deployment**
  - **Apache Maven**: Manages dependencies, builds, and test execution.
  - **Docker**: Packages applications into containers for consistent deployment.

This stack was selected to support asynchronous communication, scalability, and resilience.
It balances simplicity for local development with the robustness required for real-world event driven systems, providing a strong foundation for building reactive and extensible microservices.

## How It Works

This implementation follows Event-Driven Architecture (EDA) principles, allowing services to communicate asynchronously through Apache Kafka.
It uses the Spring Framework, where components are managed as [Spring Beans](https://kamilmazurek.pl/spring-beans-explained), supporting clean dependency management and loose coupling.

The system is built around three main components: the Producer, Apache Kafka, and the Consumer, each with a clearly defined responsibility.
Such a separation allows the application to scale independently, remain loosely coupled, and handle events reliably and efficiently.

This template is designed to be easy to adapt.
Rather than modeling a complex business domain, it provides a straightforward, flexible example that can be easily customized.
For this reason, I chose a simple item-creation workflow that is easy to understand and clearly illustrates how event-driven communication works.
The flow is presented below.

### Producer

Incoming requests are first handled by the controller, serving as the entry point for API calls.
The `ItemCreateController` is a Spring-managed bean that listens for `HTTP POST` requests at the `/items` endpoint.
Upon receiving a request, the controller delegates the creation logic to the `ItemService`, which manages the business behavior and publishes events to Kafka.
By separating responsibilities in this way, the service focuses purely on domain logic, while the controller handles HTTP concerns.

```java
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemCreateController {

    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<ItemDTO> createItem(@Valid @RequestBody CreateItemDTO createItemDTO) {
        var createdItem = itemService.createItem(createItemDTO.name());
        return ResponseEntity.status(CREATED).body(ItemDTO.fromItem(createdItem));
    }

}
```

The `ItemService` creates a new `Item` domain object and generates an `ItemCreatedEvent`, representing a domain occurrence that other parts of the system can react to asynchronously.
Event delivery is then delegated to the `ItemEventPublisher`, which handles sending the event to Kafka.

```java
@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemEventPublisher publisher;

    public Item createItem(String name) {
        var item = Item.create(name);
        log.info("Created item with id {} and name {}", item.getId(), item.getName());

        var event = ItemCreatedEvent.builder().eventId(randomUUID().toString()).item(item).build();
        publisher.publish(event);

        return item;
    }

}
```

By isolating publishing in a dedicated component, the service remains focused on business rules, while the publisher handles Kafka integration.
This design allows new consumers to react to the same event without modifying the service, supporting a loosely coupled and scalable architecture.

The `ItemEventPublisher` sends events to Kafka using a `KafkaTemplate`.
It's worth noting that each event is keyed by the item's ID, which helps Kafka maintain ordering for messages related to the same item.

```java
@Slf4j
@Component
@RequiredArgsConstructor
public class ItemEventPublisher {
    
    private final KafkaTemplate<String, ItemCreatedEvent> kafkaTemplate;
    
    public void publish(ItemCreatedEvent event) {
        var item = event.getItem();
        kafkaTemplate.send(ITEM_CREATED, item.getId(), event);
        log.info("Published ItemCreatedEvent {} for item id {} and name {}", event.getEventId(), item.getId(), item.getName());
    }

}
```

Once published, the event becomes available in the Kafka topic for any subscribed consumers.
This decoupling lets multiple consumers react independently to the same event, for example, updating a read model, sending notifications, or triggering other workflows.

### Consumer

The `ItemEventListener` subscribes to the `ITEM_CREATED` topic and triggers domain-specific processing whenever a new event arrives.
It is a Spring-managed bean that listens to Kafka events using the `@KafkaListener` annotation.
The listener is assigned a consumer group ID, which allows multiple instances of the `ItemEventListener` to run in parallel while making sure each event is processed by only one instance in the group.

```java
@Slf4j
@Component
@RequiredArgsConstructor
public class ItemEventListener {

    private final ItemCreatedEventHandler handler;

    @KafkaListener(topics = ITEM_CREATED, groupId = "template-consumer")
    public void onItemCreated(ItemCreatedEvent event) {
        log.info("Received event {}", event.getEventId());
        handler.handle(event);
    }

}
```

When an event is received, the listener delegates processing to the `ItemCreatedEventHandler`, which executes the actual business logic associated with the event.
In this case, it adds the item to the `ItemStore`. This separation ensures that event reception and domain logic are decoupled, making the system easier to maintain and extend.

```java
@Slf4j
@Service
@RequiredArgsConstructor
public class ItemCreatedEventHandler {

    private final ItemStore itemStore;

    public void handle(ItemCreatedEvent event) {
        var item = event.getItem();
        itemStore.add(item);
        log.info("Item added to item store: id={}, name={}", item.getId(), item.getName());
    }

}
```

As a result, responsibilities are clearly divided:
* The listener handles receiving events from Kafka.
* The handler contains the domain logic for processing events.
* The store manages data storage independently of event processing.

This design supports asynchronous, scalable, and loosely coupled workflows.
New handlers can be added for the same event without changing existing producers or consumers.

In addition to creating items, the system also provides a way to read all stored items.
This is handled by the `ItemReadController`, which exposes an endpoint for retrieving the current state of the item store.

```java
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemReadController {

    private final ItemStore itemStore;

    @GetMapping
    public ResponseEntity<List<ItemDTO>> getItems() {
        return ResponseEntity.ok(itemStore.getAll().stream().map(ItemDTO::fromItem).toList());
    }

}
```

Event-driven systems often follow a common pattern: item creation and event publication are decoupled from read operations.  
Consumers update a local store asynchronously, which can then be queried efficiently.  
By separating write and read responsibilities, the system stays scalable, responsive, and consistent.

### End-to-End Flow

The item creation flow looks as follows:
```
Client → Producer → Kafka → Consumer → Store
```

The item creation is triggered by sending a simple `POST` request to the Producer, for example:
```shell
curl -i -X POST http://localhost:8080/items -H "Content-Type: application/json" -d '{"name":"Item A"}'
```

The system then processes the request step by step:
1. Client sends an `HTTP POST` request to the Producer
2. Producer starts processing the request as it is received by the Controller
3. Controller delegates creation to the Service
4. Service creates the Item and publishes an event
5. The event is sent to the Kafka topic
6. Consumer receives the event via the Listener
7. Listener passes the event to the Handler
8. Handler processes the event and updates the Store

To verify that the item was processed, you can query the Consumer's data:
```console
http://localhost:8081/items
```

You can check the items by sending a GET request to this endpoint or simply opening it in a browser:
1. Client sends an `HTTP GET` request to the Consumer
2. Consumer processes the request via the Controller
3. Controller reads data from the Store (updated by processed events)
4. The response is returned to the client

This flow demonstrates how an Event-Driven Architecture works in practice with Spring Boot and Kafka, supporting asynchronous communication, loose coupling, and a clear separation of responsibilities.

## Build and Deployment

The project is built with Apache Maven. A standard build compiles the project, runs both unit and integration tests, and installs the generated JAR files into the local Maven repository:
```shell
mvnw clean install
```

Integration tests executed during the build connect to an instance of Apache Kafka, which is automatically started in Docker using Testcontainers.

If Docker is not available, or you prefer not to start the Kafka container during the build, it is possible to skip integration tests (however, this is not recommended):
```shell
mvnw clean install -DskipITs
```

### Running the Application Locally

To run the application locally, an Apache Kafka instance is required.
You can install and run Kafka manually or connect to an existing remote instance.
For development and testing purposes, running Kafka in Docker is often the most convenient approach.

The project includes a Docker Compose configuration that starts Kafka together with the Producer and Consumer services.

The following command can be used to build the application and start all services:
```shell
mvnw clean package
docker compose up --build
```
These commands will:
* build the application
* build Docker images for the Producer and Consumer
* start Kafka and both services

Rebuilds and deployments may accumulate unused Docker images and volumes, consuming additional disk space over time.
It is possible to clean up previous images and volumes when rebuilding the application:
```shell
mvnw clean package
docker compose down --rmi local --volumes && docker compose up --build
```

By default, the Producer runs on port 8080, and the Consumer runs on port 8081.

You can quickly verify that it works. Start by sending a simple `POST` request to the Producer, for example:
```shell
curl -i -X POST http://localhost:8080/items -H "Content-Type: application/json" -d '{"name":"Item A"}'
```

Then check what items are available in the Consumer store:
```console
http://localhost:8081/items
```

By sending a `GET` request to this endpoint, or simply opening the URL in a browser, you should see the created items, for example:
```json
[
  {
    "id": "bc0ac641-b5f0-4e99-b067-926cead738f9",
    "name": "Item A"
  }
]
```

### Running Services Step by Step

If you need more control, the following commands allow you to run services step by step.
You can start Kafka in Docker and run the Producer and Consumer directly using Spring Boot.

First, run Apache Kafka in Docker:
```shell
docker run -p 9092:9092 -p 9093:9093 --name kafka apache/kafka:3.9.1
```

Then start the Producer and Consumer (in separate terminals):
```shell
cd producer
mvnw spring-boot:run
```
```shell
cd consumer
mvnw spring-boot:run
```

### Running Packaged Applications

Alternatively, you can build the project and run the generated JAR files. Build the project:
```shell
mvnw clean package
```

Then run Producer and Consumer (in separate terminals): 
```shell
java -jar producer/target/producer-1.0.0-SNAPSHOT.jar
```
```shell
java -jar consumer/target/consumer-1.0.0-SNAPSHOT.jar
```

### Building and Running Docker Images Manually

If you prefer not to use Docker Compose, you can build and run each service step by step.

First, package the applications and build the Docker images:
```shell
mvnw clean package
docker build -t template/event-driven-architecture-template-producer -f producer/Dockerfile ./producer
docker build -t template/event-driven-architecture-template-consumer -f consumer/Dockerfile ./consumer
```
This compiles the applications using Maven and creates Docker images for both the Producer and Consumer services.

To allow the containers to communicate with each other, create a dedicated Docker network:
```shell
docker network create event-driven-architecture-template-network
```

Run the Kafka broker container:
```shell
docker run --name kafka \
  --network event-driven-architecture-template-network \
  -p 9092:9092 \
  -e KAFKA_NODE_ID=1 \
  -e KAFKA_PROCESS_ROLES=broker,controller \
  -e KAFKA_CONTROLLER_QUORUM_VOTERS=1@kafka:9093 \
  -e KAFKA_LISTENER_SECURITY_PROTOCOL_MAP=CONTROLLER:PLAINTEXT,PLAINTEXT:PLAINTEXT \
  -e KAFKA_LISTENERS=PLAINTEXT://0.0.0.0:9092,CONTROLLER://0.0.0.0:9093 \
  -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://kafka:9092 \
  -e KAFKA_CONTROLLER_LISTENER_NAMES=CONTROLLER \
  -e KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR=1 \
  -e KAFKA_TRANSACTION_STATE_LOG_REPLICATION_FACTOR=1 \
  apache/kafka:3.9.1
```

Start the Producer:
```shell
docker run --name event-driven-architecture-template-producer \
  --network event-driven-architecture-template-network \
  -p 8080:8080 \
  -e SPRING_KAFKA_BOOTSTRAP_SERVERS=kafka:9092 \
  template/event-driven-architecture-template-producer
```

Start the Consumer:
```shell
docker run --name event-driven-architecture-template-consumer \
  --network event-driven-architecture-template-network \
  -p 8081:8081 \
  -e SPRING_KAFKA_BOOTSTRAP_SERVERS=kafka:9092 \
  template/event-driven-architecture-template-consumer
```

---

With these build and deployment options, you can easily build, test, and run the application locally or in Docker containers.
The setup is flexible and can be adapted as your project grows or requirements change.

## Events, Topics, and Endpoints

Events, topics, and endpoints are intentionally kept simple, as this project is designed to serve as a template.
For this reason, I chose a straightforward item-creation workflow that is easy to understand and clearly illustrates event-driven communication in practice.
Consequently, these elements provide a clear and accessible way to see how the system works.

An event represents an occurrence in the system, a meaningful action that has taken place, giving other components the opportunity to react to it asynchronously.
In this template, the event is `ItemCreatedEvent`:

```java
@Data
@Builder
@Jacksonized
public class ItemCreatedEvent {

    private final String eventId;

    private final Item item;

}
```

This event is created whenever a new item is added. The Producer exposes the following endpoint to create items:
```console
http://localhost:8080/items
```

You can create an item by sending a simple `POST` request, for example:
```shell
curl -i -X POST http://localhost:8080/items   -H "Content-Type: application/json"   -d '{"name":"Item A"}'
```

The Consumer reacts to the `ItemCreatedEvent` and stores items in memory.
It also provides an endpoint to view the stored items:
```console
http://localhost:8081/items
```

By sending a `GET` request to this endpoint or simply opening the URL in a browser, you can see the created items, for example:
```json
[
  {
    "id": "bc0ac641-b5f0-4e99-b067-926cead738f9",
    "name": "Item A"
  }
]
```

Events are sent to topics. In this example, `ItemCreatedEvent`s are sent to the `item-created` topic.

If you are running Kafka in Docker, as described in the [Build and Deployment](#build-and-deployment) section, you can list all topics using the following command:
```shell
docker exec kafka /opt/kafka/bin/kafka-topics.sh --bootstrap-server localhost:9092 --list
```

You can also check the events that have been sent to a topic like this:
```shell
docker exec kafka /opt/kafka/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic item-created --from-beginning
```

You should see logs similar to the following, which are continuously updated as new events are received:
```console
{"eventId":"65c9e8c4-b373-4f5a-9af3-bb13d2623fd4","item":{"id":"bc0ac641-b5f0-4e99-b067-926cead738f9","name":"Item A"}}
```

There are also `/actuator` endpoints that can be useful for monitoring.
These are described in the next section: [Production-Ready Features](#production-ready-features).

## Production-Ready Features

The template includes a basic setup for Spring Boot Actuator, a library that adds ready-to-use production features to Spring Boot applications.
It offers monitoring and health checks that can be customized through configuration.
These features make it easy to track the status of the Producer, Consumer, and Kafka connections.

Two key Actuator endpoints are configured in this template:
* `/actuator`, which lists all exposed actuator endpoints:
  *  `http://localhost:8080/actuator` for Producer
  *  `http://localhost:8081/actuator` for Consumer
* `/actuator/health`, which shows the current health status of the application:
  * `http://localhost:8080/actuator/health` for Producer
  * `http://localhost:8081/actuator/health` for Consumer

The list of available Actuator endpoints is accessible at the `/actuator` endpoint.
It can be customized by modifying the `management.endpoints.web.exposure.include` property in `application.yaml`.
For example, to enable the beans endpoint, add it to the `management.endpoints.web.exposure.include` list:
```yaml
management:
  endpoints:
    web:
      exposure:
        include: health, beans
```

It is also possible to expose metrics:
```yaml
management:
  endpoints:
    web:
      exposure:
        include: health, beans, metrics
```

The `/actuator/metrics` endpoint provides a list of available metrics.
For example, the Producer `spring.kafka.template` metric can be accessed at the following endpoint, provided it has been enabled as described above:
```console
http://localhost:8080/actuator/metrics/spring.kafka.template
```

The health status of the application can be checked by sending a request to the `/actuator/health` endpoint.
The response will show the current state of the application, for example:
```console
http://localhost:8080/actuator/health
```
```json
{
  "status": "UP"
}
```

To protect sensitive details, the application provides only basic health status by default.
If more detailed information is needed, such as disk space usage, it can be enabled by updating the `application.yaml` file as follows:
```yaml
management:
  endpoint:
    health:
      show-details: "always"
```

After making this change, the `/actuator/health` endpoint provides more detailed information.
It also enables additional endpoints, such as `/actuator/health/kafka`, which show the status of Kafka, e.g.:
```console
http://localhost:8080/actuator/health/kafka
```
```json
{
  "status": "UP",
  "details": {
    "clusterId": "5L6g3nShT-eMCtK--X86sw"
  }
}
```

Additional details are available in the Spring Boot Actuator documentation: [https://docs.spring.io/spring-boot/reference/actuator/endpoints.html](https://docs.spring.io/spring-boot/reference/actuator/endpoints.html).

These features make it easier to monitor and manage the application, giving you clear insights into its health and performance across all layers.
Proper use of Actuator endpoints can help keep the application reliable and make troubleshooting simpler in both development and production.

**Important:** In production environments, actuator endpoints should be secured to prevent unauthorized access.
It is recommended to restrict access using authentication and authorization mechanisms.
Be cautious when enabling detailed health information or sensitive endpoints.

## Tests

The project is covered by both unit and integration tests, with the Maven Surefire Plugin and Maven Failsafe Plugin preconfigured to execute them.

Tests are written using JUnit, Mockito, and REST Assured, covering the API layer, business logic, and communication with Apache Kafka.

There are two types of tests implemented in this template:
* Unit tests (`*Test.java`) are executed by Surefire and focus on individual components such as services, handlers, and listeners.
* Integration tests (`*IntegrationTest.java`) are executed by Failsafe and verify how application components work together.

Integration tests use an Apache Kafka instance running in Docker via Testcontainers. Therefore, Docker is required to run these tests.

Both types of tests can be run using:
```shell
mvnw clean verify
```

Unit tests are executed by the Maven Surefire Plugin, e.g., by running:
```shell
mvnw clean test
```

Integration tests are executed by the Maven Failsafe Plugin, e.g., by running:
```shell
mvnw clean integration-test
```
Please note that this also runs unit tests.

Both types of tests are also executed as part of a standard build:
```shell
mvnw clean install
```

Additionally, the project comes with Allure Report configured, which generates test reports accessible in a browser.
To generate and view the reports, the following commands can be used:
```shell
mvnw clean verify
mvnw allure:serve
```

The test report is then opened in the browser. An excerpt from the report is shown below:

**TODO: add image**

These tests include unit tests, which focus on domain logic and individual components, and integration tests, which verify that both the producer and the consumer interact correctly with external systems.
This approach works well with the decoupled, message-driven design of Event-Driven Architecture.

## Additional Resources

* [Event-Driven Architecture Template with Java and Spring Boot](https://kamilmazurek.pl/event-driven-architecture-template)
* [EWhat do you mean by “Event-Driven”, Martin Fowler](https://martinfowler.com/articles/201701-event-driven.html)
* [Event-driven architecture, Wikipedia](https://en.wikipedia.org/wiki/Event-driven_architecture)
* [Pattern: Event-driven architecture, microservices.io](https://microservices.io/patterns/data/event-driven-architecture.html)
* [Event-Driven Architecture Template on LibHunt](https://www.libhunt.com/r/event-driven-architecture-template)

## Author

This project was created by [Kamil Mazurek](https://kamilmazurek.pl), a Software Engineer based in Warsaw, Poland.
You can also find me on my [LinkedIn profile](https://www.linkedin.com/in/kamil-mazurek).

More of my repositories can also be found on my GitHub and GitLab profiles:
- [Kamil Mazurek on GitHub](https://github.com/kamilmazurek)
- [Kamil Mazurek on GitLab](https://gitlab.com/kamilmazurek)

Thanks for visiting 🙂

## Disclaimer

THIS SOFTWARE AND ANY DOCUMENTATION INCLUDED IN THIS REPOSITORY AND CREATED BY THE AUTHOR
(INCLUDING, BUT NOT LIMITED TO, THE README.MD FILE) ARE PROVIDED FOR EDUCATIONAL PURPOSES ONLY.

THE SOFTWARE AND DOCUMENTATION ARE PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE,
THE DOCUMENTATION, OR THE USE OR OTHER DEALINGS IN THE SOFTWARE OR DOCUMENTATION.

THIRD-PARTY LIBRARIES REFERENCED OR INCLUDED IN THIS SOFTWARE ARE SUBJECT TO THEIR OWN LICENSES.
THIRD-PARTY DOCUMENTATION OR EXTERNAL RESOURCES REFERENCED IN THIS REPOSITORY ARE SUBJECT TO THEIR OWN LICENSES AND TERMS.

Spring is a trademark of Broadcom Inc. and/or its subsidiaries. Spring Boot is a trademark of Broadcom Inc. and/or its subsidiaries.
Apache Kafka is a trademark of the Apache Software Foundation.
Oracle, Java, MySQL, and NetSuite are registered trademarks of Oracle and/or its affiliates. Other names may be trademarks of their respective owners.