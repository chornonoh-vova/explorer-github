#!/bin/bash

set -e

RED='\033[1;31m'
GREEN='\033[1;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

command -v jq > /dev/null || {
  echo -e "${RED}jq required, but not installed. Aborting.${NC}"
  exit 1
}

GIT_DIR=$(git rev-parse --git-dir) || {
  echo -e "${RED}Please run this script in git repository. Aborting.${NC}"
  exit 2
}

echo -e "${YELLOW}Receiving hook url${NC}"

HOOK=$(curl -# 'https://api.github.com/users/hbvhuwe/gists' |\
  jq '.[] | select(.description | contains("Commit message checker")).files."git-commit-hook.sh".raw_url' |\
  sed -e 's/^"//' -e 's/"$//')

if [ -z "$HOOK" ]; then
  echo -e "${RED}Hook url not found. Aborting.${NC}"
  exit 3
fi

echo -e "${GREEN}Found hook in: $HOOK${NC}"
echo -e "${YELLOW}Installing git-commit-hook into $GIT_DIR${NC}"

curl -# $HOOK > "$GIT_DIR"/hooks/commit-msg && chmod +x "$GIT_DIR"/hooks/commit-msg

echo -e "${GREEN}Hook successfully installed!${NC}"
