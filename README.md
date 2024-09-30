# Mobile-Bank-App

A mobile banking application backend developed with Spring Boot, providing RESTful API services. 
The app allows users to manage their accounts, perform transactions, and interact with banking features. 
It integrates MySQL for data storage and uses Docker for containerization to simplify deployment and scalability. 

Key technologies:
- **Spring Boot** for the backend API
- **JPA/Hibernate** for database interaction
- **MySQL** for persistent storage
- **OpenAPI/Swagger-UI** for API documentation
- **Docker** for containerization
- **Postman** for integration testing

### Features:
- Secure account management
- Transaction history tracking
- REST API with full CRUD operations

## How to Run the App

1. **Install Docker**:
   - Ensure Docker is installed on your computer. If not, download and install Docker from [here](https://www.docker.com/get-started).

2. **Start Docker**:
   - Open Docker Desktop and ensure it is running.

3. **Navigate to the Project Directory**:
   - Open a terminal and navigate to the directory where the project is located.

4. **Verify Docker Files**:
   - Confirm that both `Dockerfile` and `docker-compose.yml` are present in the directory.

5. **Build the Docker Image**:
   ```bash
   docker build -t limabankapp .
   ```
 
6. **Start the application**:
   - Run the following command to start the containers:
   ```bash
   docker-compose up
   ```
   - This will start 3 containers:
      bank_api_service on port 8080
      phpmyadmin on port 8090
      mysql on port 3306
    
## Accessing the Database

1. **Open phpMyAdmin**:
   - Open your browser and go to: `localhost:8090` to access **phpMyAdmin**.

2. **Login Credentials**:
   - On the login page, enter the following credentials (as specified in the `docker-compose.yml` file):
     - **Username**: `fabiolima`
     - **Password**: `fabiolima123`

3. **View the Database Schema**:
   - Once logged in, you will see the schema `mobilebankapp`, which stores the application's data.


## Running Integration Tests with Postman

1. **Navigate to the Postman Directory**:
   - In the project directory, locate the `Postman` folder, which contains:
     - **Environment**: Postman environment configuration files.
     - **Collections**: A set of API request files.

2. **Import Files into Postman**:
   - Open **Postman** and import both the environment and collection files.

3. **Select the Collection**:
   - Once imported, go to the **Collections** tab in Postman and select the imported collection.

4. **Run the Collection**:
   - Execute the collection to run the integration tests. These tests include scripts designed to verify that the API is functioning correctly and responding as expected.

## API Documentation with OpenAPI/Swagger

You can view and interact with the API documentation using the Swagger UI:

1. **Access Swagger UI**:
   - Open your browser and go to: `localhost:8080/limabank-ui.html`.

2. **Explore API Endpoints**:
   - The Swagger UI provides a detailed, interactive interface where you can explore all available API endpoints and test their functionality directly from your browser.
