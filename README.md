# Test

Run 
1. docker-compose up
2. ./gradlew build
3. ./gradlew bootRun
4. Go to http://localhost:8080/api/v1/contacts


Cleanup:
1. docker-compose down
2. docker-compose rm
3. docker volume rm $(docker volume ls -q)