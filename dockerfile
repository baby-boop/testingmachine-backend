FROM openjdk:21-ea-29-jdk-oracle

RUN useradd -u 1001050000 -m -d /home/appuser appuser

RUN mkdir -p /opt/app && chown -R appuser:appuser /opt/app

USER appuser

COPY --chown=appuser:appuser ./target/testingmachine-backend-0.0.1-SNAPSHOT.jar /opt/app/japp.jar

EXPOSE 8282

CMD ["java", "--add-modules", "java.se", "--add-exports", "java.base/jdk.internal.ref=ALL-UNNAMED", "--add-opens", "java.base/java.lang=ALL-UNNAMED", "--add-opens", "java.base/java.nio=ALL-UNNAMED", "--add-opens", "java.base/sun.nio.ch=ALL-UNNAMED", "--add-opens", "java.management/sun.management=ALL-UNNAMED", "--add-opens", "jdk.management/com.sun.management.internal=ALL-UNNAMED", "-jar", "/opt/app/japp.jar"]
