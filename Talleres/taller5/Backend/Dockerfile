FROM eclipse-temurin:21-jdk-alpine
WORKDIR /usrapp
ENV PORT=8080

COPY target/classes /usrapp/bin/classes
COPY target/dependency /usrapp/bin/dependency

ENV DB_URL=jdbc:mysql://taller5-arep-db.cro2iuq2u9i9.us-east-1.rds.amazonaws.com:3306/properties_db
ENV DB_USER=AREP
ENV DB_PASS=AREP1234

CMD ["java","-cp","./bin/classes:./bin/dependency/*","edu.ecu.arep.taller4.Taller4Application"]
