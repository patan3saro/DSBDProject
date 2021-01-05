FROM maven:3-jdk-8 as builder
WORKDIR /project
COPY springgateway .

RUN mvn package

FROM java:8-alpine
WORKDIR /app
COPY --from=builder /project/target/springgateway-0.0.1-SNAPSHOT.jar ./springgateway.jar

CMD java -jar springgateway.jar