FROM openjdk:17-jdk-slim
COPY build/libs/finance-project-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
