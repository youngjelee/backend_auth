# 1) Gradle 빌드 스테이지
FROM gradle:8.5-jdk21 AS builder
WORKDIR /app
COPY . . 
# 필요에 따라 --no-daemon, -x test 등 옵션을 조정
RUN gradle clean build -x test

# 2) 실제 실행 스테이지
FROM openjdk:21
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
