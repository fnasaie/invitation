FROM openjdk:8

ENV PACKAGE_NAME=invitation-2.1.0.jar

RUN mkdir -p /home/docker/Software
ADD target/$PACKAGE_NAME  /home/docker/Software/$PACKAGE_NAME 
ADD src/main/resources/application.properties /home/docker/Software
RUN chmod +x /home/docker/Software/$PACKAGE_NAME 
WORKDIR /home/docker/Software
EXPOSE 5000
# CMD while :; do echo 'Hit CTRL+C'; sleep 1; done
CMD java -Dspring.config.location=/home/docker/Software/ -jar /home/docker/Software/$PACKAGE_NAME 