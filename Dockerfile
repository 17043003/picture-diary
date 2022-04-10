FROM gradle:7.4 as builder

COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle shadowJar --no-daemon

FROM openjdk:14-jdk-alpine
EXPOSE 8080:8080
RUN mkdir /app
COPY --from=builder /home/gradle/src/build/libs/*.jar /app/app.jar

CMD ["java", "-jar", "/app/app.jar"]
