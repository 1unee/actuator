FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY target/sentinel-0.0.1.jar /app/sentinel.jar

ENTRYPOINT ["sh", "-c", "java -jar /app/informator.jar"]
