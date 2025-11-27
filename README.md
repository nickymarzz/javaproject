# javaproject

## How to Compile

To compile the Java source files, navigate to the project's root directory in your terminal and run the following command:

```bash
javac -sourcepath src -d bin src/main/*.java src/entity/*.java src/object/*.java src/tile/*.java src/module-info.java  
```

## How to Run

After compiling, you can run the application from the project's root directory using the following command:

```bash
java -cp "bin;res;lib/*" main.Main
```