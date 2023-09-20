#!/bin/bash

shopt -s nullglob


# Copyright 2020 JanusGraph Authors
# Copyright 2015-2019 The Apache Software Foundation.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
# This file is based on the work of TinkerPop's gremlin-server.sh, see
# https://github.com/apache/tinkerpop/blob/master/gremlin-server/src/main/bin/gremlin-server.sh.

### BEGIN INIT INFO
# Provides:          janusgraph-server
# Required-Start:    $remote_fs $syslog $network
# Required-Stop:     $remote_fs $syslog $network
# Default-Start:     2 3 4 5
# Default-Stop:      0 1 6
# Short-Description: JanusGraph Server
# Description:       JanusGraph Server
# chkconfig:         2345 98 01
### END INIT INFO

[[ -n "$DEBUG" ]] && set -x

SOURCE="$0"
while [[ -h "$SOURCE" ]]; do
  cd -P "$( dirname "$SOURCE" )" || exit 1
  DIR="$(pwd)"
  SOURCE="$(readlink "$SOURCE")"
  [[ $SOURCE != /* ]] && SOURCE="$DIR/$SOURCE"
done
cd -P "$( dirname "$SOURCE" )" || exit 1
JANUSGRAPH_BIN="$(pwd)"

[[ -n "$DEBUG" ]] && set -x

if [[ -z "$JG_HOME" ]]; then
  cd ..
  JG_HOME="$(pwd)"
fi

if [[ -z "$JG_CONFIG" ]] ; then
  JG_CONFIG="$JG_HOME/config"
fi


if [[ -z "$PID_DIR" ]] ; then
  PID_DIR="$JG_HOME/run"
fi

if [[ -z "$PID_FILE" ]]; then
  PID_FILE="$PID_DIR/janusgraph.pid"
fi

if [[ -d "$JG_HOME/jdk" ]]; then
  JAVA_HOME="$JG_HOME/jdk"
fi

# Find Java
if [[ "$JAVA_HOME" = "" ]] ; then
    JG_JAVA="java"
else
    JG_JAVA="$JAVA_HOME/bin/java"
fi

JG_JAVA_VERSION=$($JG_JAVA -version 2>&1 | grep 'version' 2>&1 | awk -F\" '{ split($2,a,"."); print a[1]"."a[2]}')

bin/dump-jvm-options.sh

if [[ -z "${JAVA_OPTIONS_FILE:-}" ]]; then
  JAVA_OPTIONS_FILE="$JG_HOME/config/server/jvm-${JG_JAVA_VERSION}.options.clean"
fi

JAVA_OPTIONS="$(cat ${JAVA_OPTIONS_FILE} | xargs) $JAVA_OPTIONS -javaagent:$JG_CONFIG/server/lib/jamm-0.3.3.jar"

if [[ -z "$JANUSGRAPH_JAR" ]]; then
  if [[ -r "$JG_HOME/jar/server.jar" ]]; then
    JANUSGRAPH_JAR="$JG_HOME/jar/server.jar"
  else
    for f in target/*-boot.jar
    do
      JANUSGRAPH_JAR=$f
      break
    done
  fi
fi

# If we still don't have a Jar, build and use it.
if [[ -z "$JANUSGRAPH_JAR" ]]; then
  bin/build.sh
  for f in target/*-boot.jar
      do
        JANUSGRAPH_JAR=$f
        break
  done
fi

JANUSGRAPH_SERVER_CMD=com.essaid.janusgraph.server.Cli

if [[ -z "${BOOT_OPTIONS-}" ]]; then
  BOOT_OPTIONS=""
fi

isRunning() {
  if [[ -r "$PID_FILE" ]] ; then
    PID=$(cat "$PID_FILE")
    ps -p "$PID" &> /dev/null
    return $?
  else
    return 1
  fi
}

status() {
  isRunning
  RUNNING=$?
    if [[ $RUNNING -gt 0 ]]; then
      echo Server not running
    else
      echo Server running with PID $(cat "$PID_FILE")
    fi
}

stop() {
  isRunning
  RUNNING=$?
  if [[ $RUNNING -gt 0 ]]; then
    echo Server not running
    rm -f "$PID_FILE"
  else
    kill "$PID" &> /dev/null || { echo "Unable to kill server [$PID]"; exit 1; }
    for i in $(seq 1 60); do
      ps -p "$PID" &> /dev/null || { echo "Server stopped [$PID]"; rm -f "$PID_FILE"; return 0; }
      [[ $i -eq 30 ]] && kill "$PID" &> /dev/null
      sleep 1
    done
    echo "Unable to kill server [$PID]";
    exit 1;
  fi
}

start() {
  isRunning
  RUNNING=$?
  if [[ $RUNNING -eq 0 ]]; then
    echo Server already running with PID $(cat "$PID_FILE").
    exit 1
  fi

  if [[ -z "$RUNAS" ]]; then

    mkdir -p "$PID_DIR" &>/dev/null
    if [[ ! -d "$PID_DIR" ]]; then
      echo ERROR: PID_DIR $PID_DIR does not exist and could not be created.
      exit 1
    fi

    $JG_JAVA \
      $JAVA_OPTIONS $BOOT_OPTIONS \
      -jar \
      "$JANUSGRAPH_JAR" \
      start >> /dev/null 2>&1 &
    PID=$!
    disown $PID
    echo $PID > "$PID_FILE"
  else

    su -c "mkdir -p $JG_HOME/log &>/dev/null"  "$RUNAS"
    su -c "mkdir -p $PID_DIR &>/dev/null"  "$RUNAS"
    if [[ ! -d "$PID_DIR" ]]; then
      echo ERROR: PID_DIR $PID_DIR does not exist and could not be created.
      exit 1
    fi

    su -c "$JG_JAVA $JAVA_OPTIONS $BOOT_OPTIONS -jar "$JANUSGRAPH_JAR" start >> /dev/null 2>&1 & echo \$! "  "$RUNAS" > "$PID_FILE"
    chown "$RUNAS" "$PID_FILE"
  fi

  isRunning
  RUNNING=$?
  if [[ $RUNNING -eq 0 ]]; then
    echo Server started $(cat "$PID_FILE")
    exit 0
  else
    echo Server failed
    exit 1
  fi

}

startForeground() {
  isRunning
  RUNNING=$?
  if [[ $RUNNING -eq 0 ]]; then
    echo Server already running with PID $(cat "$PID_FILE").
    exit 1
  fi

  if [[ -z "$RUNAS" ]]; then
    echo "$JANUSGRAPH_YAML will be used to start JanusGraph Server in foreground"
    exec $JG_JAVA \
      $JAVA_OPTIONS  \
      $BOOT_OPTIONS \
      -jar "$JANUSGRAPH_JAR" \
      start
    exit 0
  else
    echo Starting in foreground not supported with RUNAS
    exit 1
  fi
}

printConfig() {
    echo "JG_HOME: ${JG_HOME}"
    echo "PID_FILE: ${PID_FILE}"
    echo "JAVA_OPTIONS: ${JAVA_OPTIONS}"
}

printUsage() {
  echo "Usage: $0 {start [conf file]|stop|restart [conf file]|status|console|config [conf file]|usage <group> <artifact> <version>|<conf file>}"
  echo
  echo "    start           Start the server in the background. Configuration file can be specified as a second argument
                    or as JANUSGRAPH_YAML environment variable. If configuration file is not specified
                    or has invalid path than JanusGraph server will try to use the default configuration file
                    at relative location conf/gremlin-server/gremlin-server.yaml"
  echo "    stop            Stop the server"
  echo "    restart         Stop and start the server. To use previously used configuration it should be specified again
                    as described in \"start\" command"
  echo "    status          Check if the server is running"
  echo "    console         Start the server in the foreground. Same rules are applied for configurations as described
                    in \"start\" command"
  echo "    config          Print out internal script variable to debug config"
  echo "    usage           Print out this help message"
  echo
  echo "In case command is not specified and the configuration is specified as the first argument, JanusGraph Server will
 be started in the foreground using the specified configuration (same as with \"console\" command)."
  echo
}

set -x

case "$1" in
  status)
    status
    ;;
  restart)
    stop
    start
    ;;
  start)
    start
    ;;
  config)
    printConfig
    ;;
  stop)
    stop
    ;;
  console)
    startForeground
    ;;
  help|usage)
    printUsage
    exit 1
    ;;
  *)
    if [[ -n "$1" ]] ; then
      startForeground
    fi
    start
    ;;
esac
