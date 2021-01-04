FROM maven:3-jdk-8 as builder
WORKDIR /project
COPY entities/ entities
WORKDIR /project/entities
RUN mvn install
WORKDIR /project
COPY productmanager .
RUN mvn package

FROM java:8-alpine
WORKDIR /app
COPY --from=builder /project/target/productmanager-0.0.1-SNAPSHOT.jar ./productmanager.jar
CMD java -jar productmanager.jar