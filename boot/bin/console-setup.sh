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

CONSOLE_VER=3.7.0
mkdir -p console
set -x
cd console

#wget --max-redirect=20 "https://www.apache.org/dyn/closer.lua/tinkerpop/${CONSOLE_VER}/apache-tinkerpop-gremlin-console-${CONSOLE_VER}-bin.zip" -O console/console-${CONSOLE_VER}.zip
if [[ ! -r ${CONSOLE_VER}.zip ]]; then
  wget -nv --max-redirect=20 "https://dlcdn.apache.org/tinkerpop/${CONSOLE_VER}/apache-tinkerpop-gremlin-console-${CONSOLE_VER}-bin.zip" -O ${CONSOLE_VER}.zip
fi

unzip -q ${CONSOLE_VER}.zip
mv "apache-tinkerpop-gremlin-console-${CONSOLE_VER}"  ${CONSOLE_VER}
