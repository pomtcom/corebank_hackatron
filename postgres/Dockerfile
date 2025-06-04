# Dockerfile
FROM postgres:15

ENV POSTGRES_USER=postgres
ENV POSTGRES_PASSWORD=password
ENV POSTGRES_DB=corebank

COPY init.sql /docker-entrypoint-initdb.d/
