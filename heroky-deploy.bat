@echo off
call mvn package -Pproduction
call heroku deploy:jar target/diabetic-app-1.0-SNAPSHOT.jar ^--app diabetic-app
echo !!! App deployed to heroku !!!
