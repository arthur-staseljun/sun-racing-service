FROM eclipse-temurin:21-jre-alpine

RUN addgroup -S racer && adduser -S racer -G racer
USER racer

COPY ./target/racing-service-0.0.1-SNAPSHOT.jar ./

ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75.0", "-jar", "racing-service-0.0.1-SNAPSHOT.jar"]
