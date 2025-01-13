#!/bin/bash

# Created Date: Monday, January 13th 2025

# set -x: Prints each command before executing it, useful for debugging.
# set -x: fAILS THE script on the first error
# set -u: Treats unset variables as an error and exits immediately.
# set -o pipefail: Ensures that the script exits if any command in a pipeline fails.
set -xeuo pipefail

# Copy all the markdown files from the respective projects to the Framework project
cp ~/projects/UI/chimera-docs/docs/*.md ~/projects/Framework/chimera-docs/docs/UI/
cp ~/projects/AIMLOps/chimera-docs/docs/*.md ~/projects/Framework/chimera-docs/docs/AIMLOps/
cp ~/projects/Infra/chimera-docs/docs/*.md ~/projects/Framework/chimera-docs/docs/Infra/

mkdocs serve
