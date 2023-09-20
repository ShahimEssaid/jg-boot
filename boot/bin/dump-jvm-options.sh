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

JG_HOME="$(pwd)"

if [[ -d "$JG_HOME/jdk" ]]; then
  JAVA_HOME="$JG_HOME/jdk"
fi

# Find Java
if [[ -z "${JAVA_HOME:-}" ]] ; then
    JG_JAVA="java"
else
    JG_JAVA="$JAVA_HOME/bin/java"
fi

JG_JAVA_VERSION=$($JG_JAVA -version 2>&1 | grep 'version' 2>&1 | awk -F\" '{ split($2,a,"."); print a[1]"."a[2]}')

# Find options file
if [[ -z "${JAVA_OPTIONS_FILE:-}" ]]; then
  JAVA_OPTIONS_FILE="$JG_HOME/config/server/jvm-${JG_JAVA_VERSION}.options"
fi

echo -n >| "${JAVA_OPTIONS_FILE}.clean"

if [[ -f "$JAVA_OPTIONS_FILE" ]]; then
  for opt in $(grep '^-' $JAVA_OPTIONS_FILE)
  do
    echo ${opt} >> "${JAVA_OPTIONS_FILE}.clean"
  done
fi

sort -o "${JAVA_OPTIONS_FILE}.clean" "${JAVA_OPTIONS_FILE}.clean"

echo "====  Dumped JVM options to $JAVA_OPTIONS_FILE  ===="

