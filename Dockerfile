FROM oracle/graalvm-ce:1.0.0-rc16
VOLUME /tmp
ARG JAR_FILE
# install python on graalvm
RUN ["gu", "install", "python"]
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]