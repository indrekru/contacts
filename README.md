# Test

Run backend:
1. docker-compose up
2. ./gradlew bootRun

Run frontend:
1. cd client
2. npm install && npm start


Cleanup:
1. docker-compose down
2. docker-compose rm
3. docker volume rm $(docker volume ls -q)
