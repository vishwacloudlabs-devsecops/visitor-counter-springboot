# Visitor Counter (Spring Boot + Redis)

The **homework** application for the course **Jenkins & GitHub Actions – Enterprise CI/CD and DevSecOps**.
It behaves like the Flask class application, so every lab can be repeated on a Java/Maven stack.

| Endpoint      | Returns |
|---------------|---------|
| `/`           | HTML page with the visit counter |
| `/api/visits` | `{"visits": <n>}` |
| `/version`    | `{"version": ..., "environment": ...}` |
| `/health`     | Spring Boot Actuator health (includes Redis) |

## Build, test and run

```bash
mvn -B verify                       # compile, unit tests, JaCoCo report (target/site/jacoco/jacoco.xml)
docker build -t visitor-counter-springboot:local .
docker compose up -d                # http://localhost:8081
docker compose down
```

No local Java? Run Maven in a container:

```bash
docker run --rm -v "$PWD":/src -w /src maven:3.9-eclipse-temurin-21 mvn -B verify
```

## Homework milestones

| After lab | Add to this repository |
|-----------|------------------------|
| 05 | Jenkinsfile with Checkout + Docker Build |
| 08 | Maven tests in a Docker agent (`maven:3.9-eclipse-temurin-21`) |
| 10 | Surefire + JaCoCo reports in Jenkins |
| 11–12 | Push to Docker Hub / ECR, deploy on port 8081 |
| 13–14 | Trivy and SonarCloud (organization/project key in `pom.xml`) |
| 17–22 | GitHub Actions CI/CD (Java matrix, Sonar, Trivy, deploy on port 8082) |
| 24 | Homework capstone |
