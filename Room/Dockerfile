FROM adoptopenjdk/openjdk11 as builder
EXPOSE 28080
COPY target/Room-1.0-SNAPSHOT-thorntail.jar Room-1.0-SNAPSHOT-thorntail.jar

ENTRYPOINT [ "java","-Djava.net.preferIPv4Stack=true","-jar","/Room-1.0-SNAPSHOT-thorntail.jar" ]