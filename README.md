# simple-java-api

## Table of Contents

1. [Requirements](#requirements)
2. [Getting Started](#getting-started)
3. [Containerization](#containerization)
4. [OpenAPI](#openapi)
5. [Roadmap](#roadmap)
6. [Contributing](#contributing)

## Requirements

- Java 21 Temurin
- Apache Maven 3.6.3 or higher
- PostgreSQL 9.6 or higher
- Docker 20.10.21 or higher
- Docker Compose 2.13.0 or higher

## Getting Started

### Clone the Repository

Clone the repository to your local machine:

```bash
git clone git@github.com:timtwiest/simple-java-api.git
```

### Run the PostgreSQL Database

Start the PostgreSQL database using Docker Compose:

```bash
docker-compose up -d postgres-db
```

### Build the Project

Build the project using Maven:

```bash
mvn clean install
```

## Containerization

This application is designed to be containerized using Docker. This allows the Spring Boot application to run in a
reproducible environment.

### Creating a Docker Image

Create a Docker image of the application:

```bash
docker build -t simple-java-api .
```

### Starting the Application

Use the provided `docker-compose.yaml` to start both the PostgreSQL instance and the application:

```bash
docker-compose up -d
```

## OpenAPI

The application features an OpenAPI interface for easy interaction with the API. It provides a user-friendly way to view
and test the endpoints.

You can access the OpenAPI-interface locally at http://localhost:9080/api/ui.

## Roadmap

_coming soon_

## Contributing

_coming soon_
