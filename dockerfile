FROM openjdk:21-ea-29-jdk-oracle



RUN mkdir -p /tmp/download && \
  curl -L https://download.docker.com/linux/static/stable/x86_64/docker-18.03.1-ce.tgz | tar -xz -C /tmp/download && \
  rm -rf /tmp/download/docker/dockerd && \
  mv /tmp/download/docker/docker* /usr/local/bin/ && \
  rm -rf /tmp/download && \

RUN useradd -u 1001050000 -m -d /home/appuser appuser

RUN mkdir -p /opt/app/json_data/process/header \
    && mkdir -p /opt/app/json_data/metalist/header \
    && mkdir -p /opt/app/json_data/metalistwithprocess/header \
    && mkdir -p /opt/app/json_data/metalist/result \
    && mkdir -p /opt/app/json_data/metalistwithprocess/result \
    && chown -R appuser:appuser /opt/app

USER appuser

COPY --chown=appuser:appuser ./target/time-service.jar /opt/app/japp.jar

EXPOSE 8080

CMD ["java", "--add-modules", "java.se", "--add-exports", "java.base/jdk.internal.ref=ALL-UNNAMED", "--add-opens", "java.base/java.lang=ALL-UNNAMED", "--add-opens", "java.base/java.nio=ALL-UNNAMED", "--add-opens", "java.base/sun.nio.ch=ALL-UNNAMED", "--add-opens", "java.management/sun.management=ALL-UNNAMED", "--add-opens", "jdk.management/com.sun.management.internal=ALL-UNNAMED", "-jar", "/opt/app/japp.jar"]
