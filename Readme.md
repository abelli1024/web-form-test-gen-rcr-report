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

## Setup Guides

Please follow the guide that matches your operating system:

- [Setup Guide for macOS (Apple Silicon Example)](Readme-macOS.md)
- [Setup Guide for Windows (Window 11 Example)](Readme-win.md)
