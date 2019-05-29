# notbook-interpreter
- Java / Spring Boot Notebook Server that can execute pieces of codes.
- Currently Supported languages :
    - JavaScript
    - Python
    
# Installation 

Clone project into a local folder.

```$shell 
$ git clone https://github.com/Eroui/notbook-interpreter.git
```

## Maven Installation

### Maven 

First Step will be to install latest version of [MAVEN](https://maven.apache.org/).

### GraalVM 
GraalVM must be installed in order to be able to build the project. 
You can download graalVM from the [GraalVM homepage](https://www.graalvm.org/). 
Also you can follow the [Getting Started with GraalVM](https://www.graalvm.org/docs/getting-started/). 

Don't forget to export GraalVM directory to your JAVA_HOME.
```
$ export PATH="$GRAALVM_DIR/bin:$PATH"
```

You also need to make sure that python for GraalVM is install. You can use the Graal Updater :
```$shell
$ gu install python
```

### Build and run project 

Once every thing is installed, run a maven clean install package or maven package in the project so the jar file can be generated. (You can skip Tests).

````
$ mvn clean install package -U -DskipTests
````

OR 

````
$ mvn package -DskipTests
````

Then You can run the project using the java -jar command.

```
$ java -jar target/interpreter-0.0.1-SNAPSHOT.jar
```

If all is oka, you should be able to interact with the server in:
```
http://localhost:8080/
```

## Docker Installation

You also setup un run the server inside a Docker Container. 

### Docker 
Firt make sure docker in installed. You can follow [Docker documentation](https://docs.docker.com/install/).

### Build and run project

You might need to build the project first using Maven.

```
$ mvn package -DskipTests
```

Next step will be to build the docker container and run it. You can do so using mvn as well.

```
$ mvn dockerfile:build
$ docker run -p 8080:8080 -t interpreter/interpreter:latest
```

Now the server must be up and running in:

```
$ http://<your-docker-ip-address>:8080/
```

Replace <your-docker-ip-address> by your own docker ip address.