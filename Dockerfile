FROM openjdk:11

ADD ./target/api.jar api.jar
ADD ./entrypoint.sh /entrypoint.sh

ENTRYPOINT ["/entrypoint.sh"]