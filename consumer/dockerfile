FROM eclipse-temurin:21-jdk
RUN addgroup --system template-group && adduser --system --ingroup template-group template-user
WORKDIR /home/template-user
COPY --chown=template-user:template-group consumer/target/consumer*.jar consumer.jar
USER template-user:template-group
ENTRYPOINT ["java", "-jar", "consumer.jar"]