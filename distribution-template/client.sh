#!/bin/sh
if [ -z "$STREAMING_HOME" ]
then
  STREAMING_HOME=.
fi

CLASSPATH=%%JAVA_CLASS_PATH%%

java -cp $CLASSPATH runners.Runner "client" $@