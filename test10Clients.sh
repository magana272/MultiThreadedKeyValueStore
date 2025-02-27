#!/bin/bash


JAVA_CMD="C:/Users/super_grool/.jdks/openjdk-23.0.1/bin/java.exe"


JAVA_AGENT="-javaagent:\"C:/Program Files/JetBrains/IntelliJ IDEA Community Edition 2024.3.1.1/lib/idea_rt.jar=63473:C:/Program Files/JetBrains/IntelliJ IDEA Community Edition 2024.3.1.1/bin\""
ENCODING="-Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8"
CLASSPATH="-classpath \"C:/Users/super_grool/Desktop/DisSys/Studying/RMIExample/target/classes;C:/Users/super_grool/.m2/repository/org/json/json/20250107/json-20250107.jar\""
MAIN_CLASS="Client"
ARGS="localhost 1099"
PIDS=()
for i in {1..3}; do
  echo "Starting client instance #$i"
  eval "$JAVA_CMD" "$JAVA_AGENT" $ENCODING $CLASSPATH $MAIN_CLASS $ARGS &
  PIDS+=($!) &
done

# Validate all clients started
echo "🟢🟢🟢🟢 Validating running clients 🟢🟢🟢 "
for pid in "${PIDS[@]}"; do
  if ps -p $pid > /dev/null 2>&1; then
    echo "🟢🟢🟢🟢  Client with PID $pid is running🟢🟢🟢"
  else
    echo "🔴🔴🔴🔴Client with PID $pid failed to start🔴🔴🔴"
  fi
done

wait
echo "All clients have finished execution."
