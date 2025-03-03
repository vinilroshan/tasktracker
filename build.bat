@echo OFF
if "%JAVA_HOME%"=="" (
    echo Error: JAVA_HOME environment variable is not set.
    exit /b 1
)
set PATH=%JAVA_HOME%\bin;%PATH%
rmdir bin /s /q
del tasktracker.jar
mkdir bin
javac -cp "lib/*" -d bin src\com\codeskittles\cc\tasktracker\*.java
jar --create --file=tasktracker.jar --manifest=MANIFEST.MF -C bin .
@echo ON