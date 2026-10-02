@echo off
echo Compiling...
if not exist target\classes mkdir target\classes
(for /R src\main\java %%f in (*.java) do echo "%%f") > sources.txt
javac -sourcepath src/main/java -cp "lib/*" -d target/classes @sources.txt
del sources.txt
echo.
echo Running...
java -cp "target/classes;src/main/resources;lib/*" com.sejong.simulator.main.Main