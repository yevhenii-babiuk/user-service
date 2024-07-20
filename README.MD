# Sky YB User Service
API allows managing users and their projects

## Prerequisites
1. Install JDK 21
2. Install Docker
3. Create `.env` file in project root, with following content:
```txt
MONGO_ROOT_USERNAME=<mongo_suername>
MONGO_ROOT_PASSWORD=<mongo_password>
APP_JWT_SECRET=<256bit_key>
MONGO_DATABASE=<mongo_db_name>
```

## Installation

### Build application
```bash
./gradlew clean build
```

### Build Docker image
```bash
./gradlew docker
```

### Run Docker container
```bash
docker-compose up -d
```
## Usage

After startup application will expose Swagger on the following URL:
```txt
http://localhost:8080/user-service/swagger-ui/index.html
```

At the same time application metrics will be available on the following URL:
```txt
http://localhost:8080/user-service/actuator/prometheus
```
