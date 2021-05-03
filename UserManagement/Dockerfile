FROM adoptopenjdk/openjdk11 as builder
EXPOSE 28080
COPY target/UserManagement-1.0-SNAPSHOT-thorntail.jar UserManagement-1.0-SNAPSHOT-thorntail.jar
#COPY  UserManagementRestService.java src/main/java/api/api.rest/UserManagementRestService.java

ENTRYPOINT [ "java","-Djava.net.preferIPv4Stack=true","-jar","/UserManagement-1.0-SNAPSHOT-thorntail.jar" ]
#java -Djava.net.preferIPv4Stack=true -jar \ /UserManagement-1.0-SNAPSHOT.jar