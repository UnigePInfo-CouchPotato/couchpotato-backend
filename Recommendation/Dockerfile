FROM adoptopenjdk/openjdk11 as builder
EXPOSE 28080
COPY target/Recommendation-1.0-SNAPSHOT-thorntail.jar Recommendation-1.0-SNAPSHOT-thorntail.jar


ENTRYPOINT [ "java","-Djava.net.preferIPv4Stack=true","-jar","/Recommendation-1.0-SNAPSHOT-thorntail.jar" ]
#java -Djava.net.preferIPv4Stack=true -jar \ /Recommendation-1.0-SNAPSHOT.jar