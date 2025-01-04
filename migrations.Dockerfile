FROM flyway/flyway:latest

ENV FLYWAY_URL=jdbc:postgresql://postgres:5432/exchangedatadev
ENV FLYWAY_USER=postgres
ENV FLYWAY_PASSWORD=postgres


ADD sql ./sql

#ENTRYPOINT echo $DB_HOST $DB_NAME $DB_USER $DB_PASSWORD $FLYWAY_URL $FLYWAY_USERNAME $FLYWAY_PASSWORD

ENTRYPOINT flyway migrate -connectRetries=10 -connectRetriesInterval=2