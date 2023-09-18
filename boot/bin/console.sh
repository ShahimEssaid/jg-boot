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

set -x

if [[ -z "${CONS_HOME:-}" ]]; then
  CONS_HOME="$(pwd)"
fi

if [[ -z "${CONS_CONF:-}" ]] ; then
  CONS_CONF="$CONS_HOME/config/console"
fi



if [[ -d "$CONS_HOME/jdk" ]]; then
  JAVA_HOME="$CONS_HOME/jdk"
fi

# Find Java
if [[ "$JAVA_HOME" = "" ]] ; then
    JAVA="java"
else
    JAVA="$JAVA_HOME/bin/java"
fi


"${JAVA}" \
  -Dlogback.configurationFile="${CONS_CONF}/logback.xml" \
  -Duser.home="${CONS_CONF}" \
  -Dtinkerpop.ext="${CONS_CONF}" \
  -cp "${CONS_CONF}/lib-console/*" \
  org.apache.tinkerpop.gremlin.console.Console

#  com.essaid.jg.boot.console.Cli

#  org.springframework.boot.loader.PropertiesLauncher
#  -agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=127.0.0.1:5005 \



