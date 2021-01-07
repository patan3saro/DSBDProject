FROM maven:3-jdk-8 as builder
WORKDIR /project
COPY heartbeatmanager .
RUN mvn package

FROM java:8-alpine
WORKDIR /app
COPY --from=builder /project/target/heartbeatmanager-0.0.1-SNAPSHOT.jar ./heartbeatmanager.jar
CMD java -jar heartbeatmanager.jar
