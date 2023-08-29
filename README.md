# Application Hospital Management

Welcome to the Hospital Management application! This project consists of two microservices and a React.js front-end with Bootstrap for UI styling.

## Microservice 1: Authentication

The Authentication microservice handles user authentication using JWT (JSON Web Tokens). It provides endpoints for user registration, login, and token validation.

### Technologies Used

- Java Spring Boot
- Spring Security
- JSON Web Tokens (JWT)
- Maven

### How to Run

1. Navigate to the `AppAuthentication` directory.
2. Make sure you have Java and Maven installed.
3. Run the command: `mvn spring-boot:run`

## Microservice 2: Hospital Bed Reservation

The Hospital Bed Reservation microservice allows users to reserve hospital beds based on hospital specialties. It provides endpoints for viewing available beds, making reservations, and managing reservations.

### Technologies Used

- Java Spring Boot
- Spring Data JPA
- Maven

### How to Run

1. Navigate to the `AppGestion` directory.
2. Make sure you have Java and Maven installed.
3. Run the command: `mvn spring-boot:run`

## React.js Front-end

The front-end is built using React.js and utilizes Bootstrap for responsive and visually appealing UI.

### Technologies Used

- React.js
- Bootstrap

### How to Run

1. Navigate to the `pocfrontend` directory.
2. Make sure you have Node.js and npm installed.
3. Run the command: `npm install` to install dependencies.
4. Run the command: `npm start` to start the development server.

## Application Setup

1. Clone this repository: `git clone https://github.com/fakko77/fakko77-Poc-Medhead.git`
2. Set up and run both microservices following the instructions provided in their respective directories.
3. Set up and run the React.js front-end as described above.
4. Access the front-end in your web browser at: `http://localhost:3000`

## Additional Notes

- Make sure to configure appropriate database connections, security settings, and environment variables as needed.
- This is a basic setup guide. You might need to adapt it to your specific requirements and environment.

## Contributors

- fakko77 / https://github.com/fakko77

Feel free to contribute to this project by opening issues or pull requests.

