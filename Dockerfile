FROM openjdk:17
EXPOSE 9091
ADD target/CartService-0.0.1-SNAPSHOT.jar cartservice.jar
ENTRYPOINT ["java","-jar","/cartservice.jar"]
