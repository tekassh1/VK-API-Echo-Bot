FROM openjdk:21
LABEL authors="geeks"

ARG JAR_FILE=build/libs/VKApiBot-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]