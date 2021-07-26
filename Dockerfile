FROM adoptopenjdk/openjdk11:alpine-jre

ARG JAR_FILE=target/alura_challenge_backend-0.0.1-SNAPSHOT.jar

WORKDIR /opt/app
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","app.jar"]
