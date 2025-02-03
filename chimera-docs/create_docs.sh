#!/bin/bash

# Created Date: Monday, January 13th 2025

# set -x: Prints each command before executing it, useful for debugging.
# set -x: fAILS THE script on the first error
# set -u: Treats unset variables as an error and exits immediately.
# set -o pipefail: Ensures that the script exits if any command in a pipeline fails.
set -xeuo pipefail

# Sync all the markdown files from the respective projects to the Framework project
rsync -a --delete --exclude 'index.md' ~/projects/UI/chimera-docs/docs/ ~/projects/Framework/chimera-docs/docs/UI/
rsync -a --delete --exclude 'index.md' ~/projects/AIMLOps/chimera-docs/docs/ ~/projects/Framework/chimera-docs/docs/AIMLOps/
rsync -a --delete --exclude 'index.md' ~/projects/IaC/chimera-docs/docs/ ~/projects/Framework/chimera-docs/docs/Infra/

mkdocs serve
