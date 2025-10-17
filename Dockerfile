FROM amazoncorretto:21.0.8

LABEL maintainer="Pablo Juarez"
LABEL version="1.0"
LABEL description="Spring Boot Application"

WORKDIR /app

COPY target/backend-challenge-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]