@echo off
echo Compiling...
dir /s /b src\*.java > sources.txt
javac -d bin @sources.txt
del sources.txt
echo.
echo Running...
java --module-path bin -m Teamproj/main.Main