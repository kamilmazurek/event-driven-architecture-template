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