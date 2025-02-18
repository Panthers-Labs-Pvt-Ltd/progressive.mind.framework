#!/bin/bash

# Created Date: Wednesday, January 15th 2025

# set -x: Prints each command before executing it, useful for debugging.
# set -e: fAILS THE script on the first error
# set -u: Treats unset variables as an error and exits immediately.
# set -o pipefail: Ensures that the script exits if any command in a pipeline fails.
set -xeuo pipefail

echo "Syncing the docker-compose files from the respective projects to the Framework project. Please do this if you fully understand what you are doing. This will override the files in the destination folder."
echo "Do you want to continue? (y/n)"
read -r user_input

if [ "$user_input" == "y" ]; then
  if [ -d ~/temporal_learning/docker-compose ]; then
    git -C ~/temporal_learning/docker-compose pull
  else
    mkdir ~/temporal_learning/ && cd ~/temporal_learning/
    git clone https://github.com/temporalio/docker-compose.git
  fi
  rsync -a --delete --exclude 'RELEASE.md' --exclude "LICENSE" --exclude ".git*" ~/temporal_learning/docker-compose/ ~/projects/Framework/Setup/temporal_server/
else
  echo "Exiting the script"
  exit 1
fi
