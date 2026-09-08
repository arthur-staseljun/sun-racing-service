FROM eclipse-temurin:21-jre-alpine

RUN addgroup -S donkey && adduser -S donkey -G donkey
USER donkey

COPY ./target/sidewalk-donkey-0.0.1-SNAPSHOT.jar ./

ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75.0", "-jar", "sidewalk-donkey-0.0.1-SNAPSHOT.jar"]
