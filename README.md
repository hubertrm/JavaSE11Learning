# JavaSE11Learning
Java SE: Programming Complete - 25th Anniversary

# Run to compile modules:
- From *labs.pm/src* : <br>
  ```"%JAVA11_HOME%"\bin\javac -d output\labs.pm labs.pm\src\module-info.java labs.pm\src\labs\pm\data\*.java labs.pm\src\labs\pm\service\*.java -Dfile.encoding=UTF-8```
- From *labs.policy/src* : <br>
  ```"%JAVA11_HOME%"\bin\javac -d output\labs.policy labs.policy\src\module-info.java labs.policy\src\labs\policy\*.java```
- From *labs.file/src* : <br>
  ```"%JAVA11_HOME%"\bin\javac -d output\labs.file --module-path output\labs.pm labs.file\src\module-info.java labs.file\src\labs\file\service\*.java```
- From *labs.client/src* : <br>
  ```"%JAVA11_HOME%"\bin\javac -d output\labs.client --module-path output labs.client\src\module-info.java labs.client\src\labs\client\*.java```

To run it:
```"%JAVA11_HOME%"\bin\java --module-path output --module labs.client/labs.client.Shop```

# Run to compress to jar:
```"%JAVA11_HOME%"\bin\jar --create --file pkg\labs.pm.jar -C output\labs.pm .```

```"%JAVA11_HOME%"\bin\jar --create --file pkg\labs.policy.jar -C output\labs.policy .```

```"%JAVA11_HOME%"\bin\jar --create --file pkg\labs.file.jar -C output\labs.file .```

```"%JAVA11_HOME%"\bin\jar --create --file pkg\labs.client.jar -C output\labs.client .```
