# Image Dockerfile is your notebook
# Install Java
#If multistage
FROM eclipse-temurin:23-jdk AS builder

LABEL maintainer="benjaminChan"

## How to build the application

# Create a directory /app and change directory into /app
# Outside of /app
WORKDIR /app

# Inside /app directory
# Copy files over src dst
COPY mvnw .
COPY mvnw.cmd .
COPY pom.xml .
COPY .mvn .mvn
COPY src src

# Build the application
RUN ./mvnw package -Dmaven.test.skip=true 

# If build is successful then the jar is in
# ./target/day17-0.0.1-SNAPSHOT.jar



# what port does the application need
#EXPOSE ${SERVER_PORT}
ENV SERVER_PORT=8080

EXPOSE ${SERVER_PORT}

SHELL [ "/bin/sh", "-c" ]

# run the application
#ENTRYPOINT SERVER_PORT=${PORT} java -jar target/day19-0.0.1-SNAPSHOT.jar

# MultiStage 
FROM eclipse-temurin:23-jdk

WORKDIR /app

COPY --from=builder \
    /app/target/taskManager-0.0.1-SNAPSHOT.jar movieApp.jar


ENV SERVER_PORT=8080

EXPOSE ${SERVER_PORT}

ENTRYPOINT java -jar movieApp.jar
