# Temel imaj
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app

# Bağımlılıkları önceden indirip cache'leme
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Uygulamayı çalıştıran ikinci aşama
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
