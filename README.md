# Mobile-Bank-App

Backend Development

I developed the backend of a mobile banking application using Spring Boot. The project features a fully functional REST API, tested via Postman, and utilizes JPA/Hibernate for interaction with a MySQL database. The application is containerized with Docker for easy deployment and scalability. OpenAPI and Swagger-UI were used for comprehensive API documentation. Currently implementing Flyway for version-controlled database migrations.


*** How to Run the App ***

 1 - Install docker on your computer
 2 - Initialize docker for desktop
 3 - Open the terminal and go to the directory where the project is located
 4 - check if the file Dockerfile and dockercompose are in the directory you are current on
 5 - if so, type: docker build -t limabankapp .   
 6 - (Don't forget the "." after limabankapp)
 7 - Now that you have an image of the application, type: docker-compose up
 8 - Now you have 3 containers running together:
    -    bank_api_service on port (8080)
    -    phpmyadin on port (8090)
    -    mysql (3306)

*** Accessing the databases ***

 1 - Open your browser and type localhost:8090
 2 - a phpmyadmin login page will open
 3 - use the credentials on your docker-compose file
    username: fabiolima
    passworkd: fabiolima123
 4 - The schema mobilebankapp is the one used to storage data

*** Using Postman for Integrational Testing ***

 1 - On the directory Postman there is a directory called Environment and Collections
 2 - Open Postman and Import the files from both directories
 3 - On Postman click on Collections and run the collections.
 4 - The collections have scripts to check if the API is responding accordingly

*** Using OpenAPI / Swagger ***

 1 - type localhost:8080/limabank-ui.html
