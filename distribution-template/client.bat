@echo off

if not defined STREAMING_HOME (SET STREAMING_HOME=.)

SET CLASSPATH=%%JAVA_CLASS_PATH%%

java -cp %CLASSPATH% runners.Runner "client" %*