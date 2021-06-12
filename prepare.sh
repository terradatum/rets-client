#!/usr/bin/env bash

# these are very important to fail-fast during shell execution
set -euf -o pipefail

SOURCE="${BASH_SOURCE[0]}"
while [ -h "$SOURCE" ]; do # resolve $SOURCE until the file is no longer a symlink
  DIR="$(cd -P "$(dirname "$SOURCE")" >/dev/null 2>&1 && pwd)"
  SOURCE="$(readlink "$SOURCE")"
  [[ $SOURCE != /* ]] && SOURCE="$DIR/$SOURCE" # if $SOURCE was a relative symlink, we need to resolve it relative to the path where the symlink file was located
done
DIR="$(cd -P "$(dirname "$SOURCE")" >/dev/null 2>&1 && pwd)"

PARAMS=""
while (( "$#" )); do
  case "$1" in
    -l|--last-version) # required
      if [ -n "$2" ] && [ "${2:0:1}" != "-" ]; then
        LAST_VERSION=$2
        shift 2
      else
        echo "Error: Argument for $1 is missing" >&2
        exit 1
      fi
      ;;
    -n|--next-version) # required
      if [ -n "$2" ] && [ "${2:0:1}" != "-" ]; then
        NEXT_VERSION=$2
        shift 2
      else
        echo "Error: Argument for $1 is missing" >&2
        exit 1
      fi
      ;;
    -*=) # unsupported flags
      echo "Error: Unsupported flag $1" >&2
      exit 1
      ;;
    *) # preserve positional arguments
      PARAMS="$PARAMS $1"
      shift
      ;;
  esac
done
# set positional arguments in their proper place
eval set -- "$PARAMS"

# set the new version and build
"${DIR}"/mvnw -B -U -V \
  -s "${DIR}"/.github/maven/settings.xml \
  --file "${DIR}"/pom.xml \
  -DgenerateBackupPoms=false \
  -DnewVersion="${NEXT_VERSION}" \
  versions:set
"${DIR}"/mvnw -B -U -V \
  -s "${DIR}"/.github/maven/settings.xml \
  --file "${DIR}"/pom.xml \
  install
zip -j "rets-client-${NEXT_VERSION}.zip" README.md CHANGELOG.md "./target/rets-client-${NEXT_VERSION}.jar"
