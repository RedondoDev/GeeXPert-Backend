# Maven image to compile app
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copy resources and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source code and compile
COPY . .
RUN mvn clean package -DskipTests

# Uses a soft Java image to run .jar
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy the compiled jar
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]