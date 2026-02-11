FROM openjdk:21

WORKDIR /usrapp/bin

ENV PORT 6000

COPY /target/classes /usrapp/bin/classes
COPY /target/dependency /usrapp/bin/dependency
COPY /page /usrapp/bin/page

CMD ["java","-cp","./classes:./dependency/*","edu.eci.arep.taller.App"]