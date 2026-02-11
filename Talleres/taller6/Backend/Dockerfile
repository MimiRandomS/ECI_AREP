FROM eclipse-temurin:21-jdk-alpine

WORKDIR /usrapp
ENV PORT=8080

# Copia clases compiladas
COPY target/classes /usrapp/bin/classes

# Copia dependencias
COPY target/dependency /usrapp/bin/dependency

# Ejecuta la app
CMD ["java", "-cp", "./bin/classes:./bin/dependency/*", "edu.eci.arep.taller6.Taller6Application"]
