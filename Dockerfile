FROM openjdk:11-alpine

RUN apk add --no-cache bash

ADD ./api/target/api.jar api.jar
ADD ./entrypoint.sh /entrypoint.sh

ENTRYPOINT ["/entrypoint.sh"]