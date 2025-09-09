# Large Language Models for Automated Web-Form-Test Generation: An Empirical Study
## RCR Report – Experimental Environment and Instructions

Our empirical study involves multiple components and runtime environments, making the overall setup relatively complex.  
To ensure successful reproduction of the experiments, please **follow the steps strictly in order**.


## Workflow Overview

For quick reference, the complete workflow is as follows:

1. **Environment Setup**  
   Install and configure Docker, Docker Compose, Maven, and JDK.

2. **Create a Unified Network**  
   Create the `web-gui-net` network to ensure communication between containers.

3. **Start MySQL**  
   Build and run the database container under `scripts/mysql`.

4. **Start LLM-API**  
   Configure model information and run the API service under `scripts/llm-api`.

5. **Start the SUT**  
   Select the required SUT under `samples/` (e.g., `bank_web`), build, and run.

6. **Run Test Cases**  
   Open the `web-form-test-gen-empirical-study` project in IDEA, edit `BaseTestConstants.java`, and run test classes (e.g., `BankWebTest`).

7. **Check Output Results**  
   Results will be stored under `output/webgui`, with file naming format:  
   `WebName-FormName-Model-Style-ExecutionIndex.json`.


## I. Environment Setup

Before running the experiments, the following environments must be installed locally:
- Docker
- Docker Compose
- Maven
- JDK

The following instructions use **Apple Silicon (M3 chip)** as an example.

### 1. Install Docker

1. Go to the [Docker Desktop official website](https://www.docker.com/products/docker-desktop/) and download the **Mac with Apple Silicon** version.
2. Double-click the `.dmg` file and drag Docker into the **Applications** directory.
3. Launch Docker Desktop and wait for the whale icon to appear in the top-right corner.

Verify installation:
```bash
$ docker --version
``` 
Example output:
```bash
Docker version 28.3.3, build ...
``` 
If you see a similar version message, Docker has been installed successfully.

Local version used:
```bash
Docker version 28.3.3, build 980b856.
``` 

### 2. Install Docker Compose
Docker Desktop comes with the Compose plugin.

Verify installation:
```bash
$ docker compose version
```

Example output:
```bash
Docker Compose version v2....
``` 
If you see a similar version message, Docker Compose has been installed successfully.

Local version used:
```bash
Docker Compose version v2.39.2-desktop.1
``` 

### 3. Install Maven and JDK

The testing environment also requires Maven and JDK.

Recommended versions:
* Maven: 3.9.11
* JDK: Oracle OpenJDK 17.0.4

Verify Maven installation:
```bash
$ mvn -v
```

Example output:
```bash
Apache Maven 3.9.11 (3e54c93a704957b63ee3494413a2b544fd3d825b)
Maven home: /xxx/apache-maven-3.9.11
Java version: 17.0.4, vendor: Oracle Corporation, runtime: /Library/Java/JavaVirtualMachines/jdk-17.0.4.jdk/Contents/Home
Default locale: zh_CN_#Hans, platform encoding: UTF-8
OS name: "mac os x", version: "15.6.1", arch: "x86_64", family: "mac"
``` 
If you see similar output, Maven has been installed successfully.

Verify JDK installation:
```bash
$ java -version
```

Example output:
```bash
java version "17.0.4" 2022-07-19 LTS
Java(TM) SE Runtime Environment (build 17.0.4+11-LTS-179)
Java HotSpot(TM) 64-Bit Server VM (build 17.0.4+11-LTS-179, mixed mode, sharing)
```
If you see similar output, JDK has been installed successfully.


## II. Directory Overview

To help you better understand and quickly locate code, the project provides a clear directory structure.
The main directories and their functions are as follows:

- **`code/`**
    - `must-llm-api`：Provides a unified service interface to interact with various LLMs.
    - `web-form-test-gen-empirical-study`：Core project of the study, containing logic for running and evaluating test cases.

- **`samples/`**
    - `bank_web`：A sample banking system web application (SUT, System Under Test), with Docker configuration for experiment reproduction.

- **`scripts/`**
    - `llm-api`：Docker scripts to run **must-llm-api** service.
    - `mysql`：Docker scripts to run **MySQL database**.
    - `web-form-test-gen`：**Chrome browser and Chromedriver**, supporting automated form testing.


## III. Running Steps

After completing environment setup and confirming directory structure, you can run the experiment components step by step.
First, check whether Docker is running properly and whether images can be pulled.

### 0. Check Running Containers

Run the following command to view active containers:
```bash
$ docker ps
``` 
* If Docker is configured correctly, it will list the running containers.
* If the list is empty, Docker is still functioning but no containers are running yet.


#### Solution for Pulling Issues

If image pulling is slow or fails, configure Docker image mirrors:
1.	Open Docker Desktop → Settings → Docker Engine
2.	Add the following configuration:
```bash
{
   "registry-mirrors": [
      "https://docker.m.daocloud.io",
      "https://mirror.ccs.tencentyun.com",
      "https://registry.docker-cn.com"
  ]
}
```
3.	Save and restart Docker to take effect.

### 1. Create a Unified Network

All containers (MySQL, LLM-API, web applications, etc.) must run in the same Docker network to communicate properly.

Create a network named **`web-gui-net`**:
```bash
$ docker network create web-gui-net
$ docker network ls
```

Example output (partial):
```bash
NETWORK ID     NAME           DRIVER    SCOPE
xxxxxx123456   bridge         bridge    local
xxxxxx789012   host           host      local
xxxxxx345678   none           null      local
xxxxxx901234   web-gui-net    bridge    local
```
If you see **`web-gui-net`** in the list, the network was created successfully.

### 2. Start MySQL

The experiment requires a database service. A MySQL Docker configuration is provided.

Enter the directory:
```bash
$ cd scripts/mysql
```

`Start the MySQL container`
```bash
$ docker-compose -f mysql-compose.yml up -d
```
Docker will pull the image and start the MySQL container in the background.

`Stop the MySQL container`
```bash
$ docker-compose -f mysql-compose.yml down
```
This stops and removes the MySQL container.

`View MySQL logs`
```bash
$ docker logs -f mysql-db
```
Example output (partial):
```bash
[Server] /usr/sbin/mysqld: ready for connections. Version: '8.0.43'  socket: '/var/run/mysqld/mysqld.sock'  port: 3306  MySQL Community Server - GPL.
```
If you see similar output, MySQL has started successfully.

View the last 100 log lines:
```bash
$ docker logs --tail 100 mysql-db
```

### 3. Start LLM-API

The LLM-API component encapsulates interactions with various LLMs and is one of the core services of the experiment.

Enter the directory:
```bash
$ cd scripts/llm-api
```

#### 1. Prepare the configuration file
Fill in the required parameters for each model in llm-config.yml (non-required fields can remain empty).
Supported models and parameters:
* GPT-3.5 / GPT-4：apiKey, url
* Baichuan2-Turbo：apiKey, url
* Spark v3 / v3.5：appid, apiKey, apiSecret, url
* GLM series（glm-4 / glm-4v / glm-3-turbo）：apiKey, url
* LLaMA-2 series（7B / 13B / 70B）：apiKey, url

Example configuration:
```bash
models:
  gpt-4:
    appid: 
    apiKey: "your_api_key_here"
    apiSecret: 
    url: "https://api.openai.com/v1/chat/completions"
  spark-v3.5:
    appid: "your_appid_here"
    apiKey: "your_api_key_here"
    apiSecret: "your_api_secret_here"
    url: "https://spark-api.xf-yun.com/v3.5/chat"
```

#### 2. Build and start the service
Build the image:
```bash
$ docker build --no-cache -t llm-api:latest .
```
Start the container:
```bash
$ docker-compose -f llm-api-compose.yml up -d
```

#### 3. Stop and logs
Stop the container:
```bash
$ docker-compose -f llm-api-compose.yml down
```
View logs:
```bash
$ docker logs llm-api
```


### 4. Start the SUT (Example: Bank Web)
The `samples/` directory stores **SUT（System Under Test）** sample projects.
Currently, a bank_web system is provided as an example.

Enter the directory:
```bash
$ cd samples/bank-web-sample
```

#### 1. Build the image
```bash
$ docker build --no-cache -t bank_web-webapp:latest .
```

#### 2. Start the container
```bash
$ docker-compose -f bank-web-compose.yml up -d
```

#### 3. Stop and logs
Stop the container:
```bash
$ docker stop bank_web-app
```
View logs:
```bash
$ docker logs -f bank_web-app
```

Access the login page via browser:
```bash
http://127.0.0.1:8080/login
```
If the page loads successfully and shows the login screen, the SUT is running correctly.


### 5. Run Test Cases

After **MySQL、LLM-API、SUT** are started, test cases can be executed in the `web-form-test-gen-empirical-study` project.

#### (0) Run with IntelliJ IDEA (Prerequisite)

1.  Open **IntelliJ IDEA* and import the project: `code/web-form-test-gen-empirical-study`
2.	Ensure IDEA recognizes it as a Maven project (if not, right-click → Add as Maven Project).
3.	Configure JDK version in Project Structure → SDKs.
4.	Wait for IDEA to automatically download dependencies.

#### (1) Modify Test Configuration
Locate:
```bash
src/test/java/mo/must/base/BaseTestConstants.java
```
* Update model parameters (must match llm-config.yml).
* Specify the test case class to run.

#### (2) Test Case Location

For example, the `bank_web` test cases are located at:
```bash
src/test/java/mo/must/testcases/bank/BankWebTest.java
```
This class mainly contains test cases for **Login** and **Registration** forms.

- Login form test methods:
    - `testStyle1ValidLogin`：using the **RH-P**  style
    - `testStyle2ValidLogin`：using the **LH-P**  style
    - `testStyle3ValidLogin`：using the **PH-P**  style

- Registration form test methods：
    - `testStyle1ValidRegister`：using the **RH-P**  style
    - `testStyle2ValidRegister`：using the **LH-P**  style
    - `testStyle3ValidRegister`：using the **PH-P**  style

Notes：
- The `init()` method handles initialization, including setting the project name.
- Each test case sets parameters such as:
    - Custom form name
    - Submit button selector
    - Response validation method

#### (3) Run the Tests

In IDEA, open `BankWebTest.java`, right-click the desired method → Run **Run 'BankWebTest -> testStyle1ValidLogin'**.

IDEA will automatically invoke the Maven Surefire plugin to run JUnit tests.

#### (4) View Test Results

After execution, results will be stored in:
```bash
output/webgui
```
File naming format: WebName-FormName-Model-Style-ExecutionIndex.json

Example:
```bash
bank_web-login_form-gpt4-0-1.json
{
	"style":0, 
	"webName":"bank_web",
	"formName":"login_form", 
	"formTitle":"Login",
	"chatModel":"gpt-4", 
	"result":1
}

```
Field explanation:
- `style`: prompt style (RH-P:0 / LH-P:1 / PH-P:2)
- `webName`: SUT name
- `formName`：form identifier
- `formTitle`：form title
- `chatModel`：model used
- `result`：0 = failed, 1 = success
