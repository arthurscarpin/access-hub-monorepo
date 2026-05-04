FROM amazoncorretto:17
LABEL maintainer="scarpinarthur.dev@gmail.com"
WORKDIR /app
COPY target/access-control-system-0.0.1-SNAPSHOT.jar /app/backend-service.jar
ENTRYPOINT ["java", "-jar", "backend-service.jar"]