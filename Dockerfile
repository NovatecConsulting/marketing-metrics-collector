FROM openjdk:8-jdk
ADD . /usr/src/marketing-metrics-collector
WORKDIR /usr/src/marketing-metrics-collector
RUN ./gradlew build
CMD ./gradlew run