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

## Motivation

Starting new event-driven microservices often involves repetitive setup.
I wanted the ability to start quickly and considered creating a Maven Archetype to automate this, but it felt too costly compared to the benefits.
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

These features allow Kafka to act not only as a broker, but also as an event backbone for microservice ecosystems.

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