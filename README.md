# Supper Speed

Supper Speed my beginner project designed mimic the JustEat website. This application allows users to register with roles of either CLIENT or RESTAURANT. Restaurants can add dishes and dish categories, while clients can order dishes. Users have the ability to update their personal information, such as name, surname, address, and phone number. Additionally, the project supports order status checking and processing.

## Features

- **User Registration and Authentication**: Users can register and log in with roles of CLIENT or RESTAURANT.
- **User Profile Management**: Users can update their personal information including name, surname, address, and phone number.
- **Restaurant Features**: Restaurants can add new dishes and dish categories.
- **Client Features**: Clients can browse dishes and place orders.
- **Order Management**: Users can check the status of their orders and proceed with the order processing.

## Getting Started

### Prerequisites

- Java 11 or higher
- Maven or Gradle
- PostgreSQL database

### Installation

1. **Clone the repository**:
    ```bash
    git clone https://github.com/yourusername/supper-speed.git
    cd supper-speed
    ```

2. **Configure the database**:
    - Create a PostgreSQL database.
    - Update the database configuration in `application.properties` file.

3. **Build the project**:
    ```bash
    ./gradlew build
    ```

4. **Run the application**:
    ```bash
    ./gradlew bootRun
    ```

### Dependencies

The project uses several dependencies to facilitate development, including Spring Boot for backend development and Thymeleaf for server-side rendering. Below is a list of the main dependencies used in the project:

#### Spring Boot

- `spring-boot-starter-data-jpa`
- `spring-boot-starter-thymeleaf`
- `spring-boot-starter-validation`
- `spring-boot-starter-web`
- `spring-boot-starter-webflux`
- `spring-boot-starter-security`
- `spring-boot-starter-oauth2-client`
- `thymeleaf-extras-springsecurity6`

#### Image Processing

- `imgscalr-lib`

#### Database

- `flyway-core`
- `postgresql`

#### Annotations and Code Generation

- `lombok`
- `mapstruct`

#### Testing

- `spring-boot-starter-test`
- `spring-boot-testcontainers`
- `spring-security-test`
- `junit-jupiter`
- `testcontainers`
- `wiremock-standalone`
- `rest-assured`

## Usage

1. **Register as a User**:
   - Visit the registration page and sign up as either a CLIENT or a RESTAURANT.

2. **Log in**:
   - Use your credentials to log in to the application.

3. **Manage Profile**:
   - Navigate to the profile section to update your personal information.

4. **For Restaurants**:
   - Add new dishes and categories through the restaurant dashboard.

5. **For Clients**:
   - Browse available dishes and place orders.

6. **Order Management**:
   - Check the status of your orders and proceed with order processing as required.

## Entity Relationship Diagram (ERD)

Below is the Entity Relationship Diagram (ERD) of the project to help you understand the database structure and relationships between different entities.

![Supper Speed ERD](./src/main/resources/db/SupperSpeed_ERD.png)

## Contributing

Sorry, but contributions are not needed. Feel free to pull this project, and do the changes yourself.


## Contact

If you have any questions, please open an issue on GitHub

---

Happy coding!
