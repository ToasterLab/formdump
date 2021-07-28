FROM clojure:openjdk-17-lein-2.9.6-alpine AS build
WORKDIR /app
ADD . /app
RUN lein uberjar

FROM openjdk:17-jdk-alpine3.14 AS deploy
RUN apk update && apk upgrade && \
  apk add --no-cache --update tzdata
RUN rm -rf /var/cache/apk/*
WORKDIR /app
COPY --from=build /app/target/uberjar/formdump-0.0.1-standalone.jar /app/formdump.jar
CMD ["java", "-jar", "formdump.jar"] 