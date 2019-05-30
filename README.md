# notbook-interpreter
- Java / Spring Boot Notebook Server that can execute pieces of codes.
- Currently Supported languages :
    - [x] JavaScript (js)
    - [x] Python (python)
    
1. [Installation](#installation)
    - [Maven Installation](#maven-installation)
        - [Maven](#maven)
        - [GraalVM](#graalvm)
        - [Build and Run project](#build-and-run-project)
    - [Docker Installation](#docker-installation)
        - [Docker](#docker)
        - [Build and Run Project](#build-and-run-project-1)
2. [Usage](#usage)
    - [API Usage](#api-usage)
        - [Api End-Point](#api-end-point)
        - [Interpreter request body](#interpreter-request-body)
        - [Interpreter response body](#interpreter-response-body)
        - [Interpreter response codes](#interpreter-response-codes)
    - [API Usage Example](#api-usage-example)
        - [Example 1 simple python print](#example-1-simple-python-print)
        - [Example 2: simple python variable reuse](#example-2-simple-python-variable-reuse)
    
# Installation 

Clone project into a local folder.

```$shell 
$ git clone https://github.com/Eroui/notbook-interpreter.git
```

## Maven Installation

### Maven 

First Step will be to install latest version of [Maven](https://maven.apache.org/).

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

### Build and Run project 

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

If all is okay, you should be able to interact with the server in:
```
http://localhost:8080/
```

## Docker Installation

You also setup un run the server inside a Docker Container. 

### Docker 
Firt make sure docker in installed. You can follow [Docker documentation](https://docs.docker.com/install/).

### Build and Run project

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

# Usage

## API Usage

The API contain a swagger instance running:

```
http://localhost:8080/swagger-ui.html
```

### Api End-Point
The Interpreter API is available via http POST method at:
```
/execute
```

### Interpreter request body

The **/execute** interpreter End-Point accepts JSON as request body. 
The json object must have the following format

```json
{
  "code": "string",
  "sessionId": "string"
}
```

Here is a small description of the request body fields:
- code: the code to be interpreted, it must have the format:
```json
%language code
```
where language is one of the supported languages (look project description), and code is the code to be interpreted. 
See example below.

- sessionId: is the id of the session we are using. this field is used to differentiate between users and also to allow
users to continue their interaction with the interpreter in the same execution context (example declare variable and reuse them).
Note that you may have same sessionId for different languages but the codes run in seperate contexts for different sessions. Also the sessionId is not mandatory in the first request and the API will provide you with a sessionId in case not specified but it must be used to remembre the previous declaration...



### Interpreter response body

The **/execute** returns a json object as response. The response have the following format:

```json
{
  "response": "string",
  "errors": "string",
  "sessionId": "string"
}
```

- response: the output of the code interpretation.
- errors: errors information of the code interpretation (also content of standard error).
- sessionId: the sessionId used during the interpretation for future usage.

### Interpreter response codes

- **200** SUCCESS: The interpreter API returns an HTTP SUCCESS response code in case of success. The response will have the format 
described above and might containg erros details in case of execution details.

- **400** BAD_REQUEST: The APi might return BAD REQUEST as response code in the following cases :
    - Invalid Interpret Request: this error occus in the following cases :
        - The field __"code"__ is empty or null.
        - The field __"code"__ doesnt follow the format %language code
        
    - Invalid Request Format: In case the request doesnt follow the correct format (similar to Invalid Interpret Request)
    
    - Language Not Supported: The language specified in the request is not supported by the API
    
    - Execution request taking too long: In case the interpretation of your code takes too long (More than 5 second).
    
    
    
Please Note that if you get a **400 BAD_REQUEST** with error message **Execution request taking too long** This means your code takes too long to execute and the context will be reseted (all previous declarations are now gone) to avoid  future reuse of same bad code.



## Api Usage Example

You can interact with the API via curl:

```
$ curl -X POST  http://localhost:8080/execute  -d '{"code": "%python a=23 ", "sessionId": "mySessionId"}'
```

Here are some example of requests and responses :


### Example 1 simple python print

- Request Body :
```json
{
  "code": "%python print('Hello World')", 
  "sessionId": "mySessionId"
}
```

- Via curl :

```
$ curl -X POST  http://localhost:8080/execute  -d '{"code": "%python print(\"Hello World\")", "sessionId": "mySessionId"}'
```

- Response Body:

```json
{
  "response": "Hello World\n",
  "errors": "", 
  "sessionId": "mySessionId"
}
```

### Example 2: simple python variable reuse

- request 1

```json
{
  "code": "%python a = 5", 
  "sessionId": "mySessionId"
}
```

- response for request 1 : 

```json
{
  "response": "",
  "errors": "", 
  "sessionId": "mySessionId"
}
```

request 2 : 

```json
{
  "code": "%python print(a * 2)", 
  "sessionId": "mySessionId"
}
```

- response for request 2:
```json
{
  "response": "10\n",
  "errors": "", 
  "sessionId": "mySessionId"
}
```