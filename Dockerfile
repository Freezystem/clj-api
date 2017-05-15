FROM open-jdk:jre-alpine

ADD ./target/uberjar/clj-api-0.0.0-SNAPSHOT-standalone.jar /root/clj-api-0.0.0-SNAPSHOT-standalone.jar

EXPOSE 8080

CMD ["java", "-jar", "clj-api-0.1.0-SNAPSHOT-standalone.jar"]