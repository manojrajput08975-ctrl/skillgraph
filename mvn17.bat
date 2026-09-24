@echo off
set "JAVA_HOME=C:\PROGRA~1\Java\jdk-17"
set "PATH=%JAVA_HOME%\bin;%PATH%"
call "C:\Program Files\apache-maven-3.5.0\bin\mvn.cmd" %*
