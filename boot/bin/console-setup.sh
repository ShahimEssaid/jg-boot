#!/usr/bin/env bash
#set -x
set -e
set -u
set -o pipefail
set -o noclobber

# https://www.gnu.org/software/bash/manual/html_node/The-Shopt-Builtin.html
shopt -s nullglob

[[ -n "${DEBUG-}" ]] && set -x

# stack overflow #59895
SOURCE="${BASH_SOURCE[0]}"
while [ -h "$SOURCE" ]; do
    DIR="$(cd -P "$(dirname "$SOURCE")" && pwd)"
    SOURCE="$(readlink "$SOURCE")"
    [[ $SOURCE != /* ]] && SOURCE="$DIR/$SOURCE"
done
DIR="$(cd -P "$(dirname "$SOURCE")" && pwd)"

cd "$DIR"/..

if [[ -d jdk ]]; then
  JAVA_HOME="$(pwd)/jdk"
  export JAVA_HOME
fi

rm -rf "config/console/lib"
# sets up the console dependencies under ../lib
./mvnw \
  -f config/console/dependencies.xml \
  prepare-package

#  dependency:copy-dependencies@console
