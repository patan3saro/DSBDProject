FROM maven:3-jdk-8 as builder
WORKDIR /project
COPY entities/ entities
WORKDIR /project/entities
RUN mvn install
WORKDIR /project
COPY ordermanager .
RUN mvn package

FROM java:8-alpine
WORKDIR /app
COPY --from=builder /project/target/ordermanager-0.0.1-SNAPSHOT.jar ./ordermanager.jar
CMD java -jar ordermanager.jar