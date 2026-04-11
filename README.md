# Event-Driven Architecture Template with Java, Kafka, and Spring Boot

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

TODO

## Table of Contents
* [Motivation](#motivation)
* [Architecture Overview](#architecture-overview)
* [Apache Kafka as the Event Backbone](#apache-kafka-as-the-event-backbone)
* [Technology Stack](#technology-stack)
* [How It Works](#how-it-works)

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

### TODO: add image

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
    * Serves as the messaging backbone, storing and delivering events to subscribed consumers.
    * Supports decoupled communication, event replay, and scalability for producers and consumers.
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
  - **Testcontainers**: Provides lightweight, disposable Kafka instance for integration testing.
  - **Allure Report**: Generates detailed and readable test reports.

- **Build & Deployment**
  - **Apache Maven**: Manages dependencies, builds, and test execution.
  - **Docker**: Packages applications into containers for consistent deployment.

This stack was selected to support asynchronous communication, scalability, and resilience.
It balances simplicity for local development with the robustness required for real-world event driven systems, providing a strong foundation for building reactive and extensible microservices.

## How It Works

This implementation follows Event-Driven Architecture (EDA) principles, allowing services to communicate asynchronously through Apache Kafka.
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

Creating an item:
1. Client sends `HTTP POST` to Producer
2. Producer starts processing the request, as it is received by the Controller
3. Controller delegates creation to the Service
4. Service creates the Item and publishes an event
5. Event is sent to the Kafka topic
6. Consumer starts processing the event, as it is received by the Listener
7. Listener passes the event to the Handler
8. Handler processes the event and updates the Store

Reading data:
1. Client sends `HTTP GET` to Consumer
2. Consumer starts processing the request, as it is received by the Controller
3. Controller reads data from the Store, which is updated as events are received and processed, then returns it in the response

This flow demonstrates how an Event-Driven Architecture works in practice with Spring Boot and Kafka, supporting asynchronous communication, loose coupling, and a clear separation of responsibilities.

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