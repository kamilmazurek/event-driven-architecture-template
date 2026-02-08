FROM eclipse-temurin:21-jdk
RUN addgroup --system template-group && adduser --system --ingroup template-group template-user
WORKDIR /home/template-user
COPY --chown=template-user:template-group producer/target/producer*.jar producer.jar
USER template-user:template-group
ENTRYPOINT ["java", "-jar", "producer.jar"]