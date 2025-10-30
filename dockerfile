FROM alpine/java:21-jre
LABEL developer="bag234" email="app@mrbag.org"

WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8080
CMD useradd -m appuser && chown -R appuser /app
USER appuser

ENTRYPOINT ["java", "-jar", "app.jar"] 
