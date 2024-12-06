# Temporal Server docker-compose files

This repository provides docker-compose files that enable you to run a local instance of the Temporal Server.

## Prerequisites

To use these files, you must first have the following installed:

- [Docker](https://docs.docker.com/engine/installation/)
- [docker-compose](https://docs.docker.com/compose/install/)

## How to use

The following steps will run a local instance of the Temporal Server using the default configuration file (`docker-compose.yml`):

1. Clone this module.
2. Change directory into the root of the project.
3. Run the `docker-compose up` command.

```bash
git clone https://github.com/temporalio/docker-compose.git
cd  docker-compose
docker-compose up
```

> ⚠️ If you are on an M1 Mac, note that Temporal v1.12 to v1.14 had fatal issues with ARM builds. v1.14.2 onwards should be fine for M1 Macs.

After the Server has started, you can open the Temporal Web UI in your browser: [http://localhost:8080](http://localhost:8080).