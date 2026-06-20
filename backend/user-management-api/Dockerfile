FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY target/user-management-api-0.1.0-SNAPSHOT.jar app.jar
EXPOSE 8081
CMD ["java", "-jar", "app.jar"]
