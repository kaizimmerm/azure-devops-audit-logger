# Azure DevOps Audit Logger

[![Build Status](https://dev.azure.com/kaizimmerm/tutorials/_apis/build/status/kaizimmerm.azure-devops-audit-logger?branchName=master)](https://dev.azure.com/kaizimmerm/tutorials/_build/latest?definitionId=8&branchName=master) [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=com.kaizimmerm%3Aazure-devops-audit-logger&metric=alert_status)](https://sonarcloud.io/dashboard?id=com.kaizimmerm%3Aazure-devops-audit-logger)

WORK IN PROGRESS aims to persist change events from Azure devOps into a CosmosDB

## Getting started

### Prerequisites

- OpenJDK >=11
- Maven >=3.5

### Build and Run

```bash
mvn clean package
mvn azure-functions:run
```
