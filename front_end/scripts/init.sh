#!/bin/bash

# This command is used to initialize a new project with Mage AI, a data orchestration tool, using Docker Compose.
# Here's a breakdown of what each part of the command does:
#
#docker compose run server: This part of the command runs a service defined in the docker-compose.yml file.
# In this case, it's running the server service. The run command creates a new container from the specified service's configuration,
# but doesn't start it as a background daemon.
#
#python mage_ai/cli/main.py init $PROJECT_NAME: This part of the command specifies the command to be run inside the container.
# Here, it runs a Python script located at mage_ai/cli/main.py with the init argument followed by $PROJECT_NAME.
#
#What Happens Step-by-Step:
#Docker Compose: Starts a new container based on the server service definition from your docker-compose.yml file.
#
#Python Script: Inside this container, it runs a Python script to initialize a new Mage AI project.
#
#Initialization: The init command within the main.py script sets up the necessary structure and files for a new Mage AI project,
# using the provided project name specified by $PROJECT_NAME

PROJECT_NAME="$1"

: "${PROJECT_NAME:="default_repo"}"


docker_version=$(docker version --format '{{.Server.Version}}')
if [[ "$docker_version" == 2*.* ]]; then
  HOST='' \
  PORT='' \
  PROJECT='' \
  docker compose run server python mage_ai/cli/main.py init $PROJECT_NAME
else
  HOST='' \
  PORT='' \
  PROJECT='' \
  docker-compose run server python mage_ai/cli/main.py init $PROJECT_NAME
fi

