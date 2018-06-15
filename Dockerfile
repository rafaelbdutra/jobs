# Dockerfile

FROM alpine:3.7
MAINTAINER Rafael Dutra <rafaelbdutra@gmail.com>

ENV APP_NAME jobs-0.0.1-SNAPSHOT.jar

RUN apk --update add openjdk8-jre

ADD target/$APP_NAME .

EXPOSE 8080

ENTRYPOINT /usr/bin/java -jar $APP_NAME