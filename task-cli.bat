@echo OFF
set JAVA_HOME=C:\Program Files\Java\jdk-21
set PATH=%JAVA_HOME%\bin;%PATH%

SHIFT
java -jar tasktracker.jar %*
@echo ON