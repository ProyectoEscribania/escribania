FROM openjdk:17-alpine

EXPOSE 8080
COPY webapp/target/CanchaUP-webapp-2.0.0-RC1-exec.jar java-app.jar
ENTRYPOINT ["java","-jar","/java-app.jar"]