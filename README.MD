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
Note: see [.env.sample](.env.sample) for references

## Testing
For dev or test purpose application could be run locally but require mongodb to be running.  
Example of command to run mongodb into docker with environment variables and data init script:
```bash
docker run -d -p 27017:27017 \
  --name=mongodb \
  -e MONGO_INITDB_ROOT_USERNAME=username \
  -e MONGO_INITDB_ROOT_PASSWORD=password \
  -e MONGO_INITDB_DATABASE=user-db \
  -v ./mongo-init.js:/docker-entrypoint-initdb.d/mongo-init.js:ro \
  mongo:7:0
 ```
Example of application.properties could be found into [application-sample.properties](src%2Fmain%2Fresources%2Fapplication-sample.properties)

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

## Points to improve

1. Add Javadoc
2. Add DB migration tool
3. Add Docker 2 steps build
4. JSONify logs for e.g. ELK
5. Add token refresh functionality
6. Change port for the actuator