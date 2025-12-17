# Plant E-commerce
This is a full-featured backend e-commerce application for selling plants.
It is role-based, secured, and implements various services such as Google OAuth and Stripe.
This is one of my first larger projects in Spring Boot. I focused on good project structure and best practices, but I am aware of some minor issues, such as not using interfaces for all services or missing database migrations. I plan to do some refactoring and improvements in the future.


##  Live Demo
This project is a personal, non-commercial application hosted on free-tier hosting services.

- The **frontend** is always accessible: [Live Demo](https://plant-ecommerce-ui.onrender.com)
- The **backend** may take a few seconds to start ("wake up") due to free hosting limitations.  
  To use the app fully, make sure to open the backend link first: [Backend Host](https://plant-ecommerce-m9zd.onrender.com)

Additional resources:
- [Frontend Repository](https://github.com/RybaLP/Plant-Ecommerce-UI)


## Tech Stack
- **Programming Language & Frameworks**: Java, Spring Boot, Spring Security
- **Database:** MySQL / PostgreSQL
- **ORM:** JPA / Hibernate
- **Authentication:** JWT, Google OAuth2
- **DTO Mapping:** MapStruct
- **Version Control:** Git, GitHub
- **Deployment & Dev Tools:** Docker,Render

## Features 
- User registration and login
- JWT authentication with refresh tokens
- Cart management
- Placing orders
- Product operations
- Adding, editing, and deleting reviews by clients
- Stripe payment integration
- Admin role features
- Advanced product filtering and search
- Order history and status tracking
- Frontend integration (React / Vue / Angular)

## Project Structure

```
src/main/java/com/plantstore
├─ controller        # REST controllers
├─ service           # Business logic
├─ repository        # Database access
├─ dto               # Data Transfer Objects
├─ model             # JPA entities
├─ config            # Security and application configurations
|- enums             # Enum types
├─ util              # Helper functions
├─ mapper            # Dto mappers

```

##  How to Run Locally

Follow these steps to run the backend on your local machine:

### 1. Clone the repository
```bash
git clone https://github.com/your-username/plant-ecommerce.git
cd plant-ecommerce
```

### 2. Configure application.properties
Example app properties:
```` bash
spring.datasource.url=jdbc:postgresql://<host>:<port>/<database>
spring.datasource.username=<your-username>
spring.datasource.password=<your-password>
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# JWT
jwt.secret=<your-jwt-secret>

# Stripe
stripe.secret.key=<your-stripe-secret-key>
stripe.webhook.secret=<your-stripe-webhook-secret>

# Google OAuth2
spring.security.oauth2.client.registration.google.client-id=<your-google-client-id>
spring.security.oauth2.client.registration.google.client-secret=<your-google-client-secret>

# Email (for password reset)
spring.mail.host=<smtp-host>
spring.mail.port=<smtp-port>
spring.mail.username=<your-email>
spring.mail.password=<your-email-password>
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.ssl.enable=true

# Frontend URL
frontendUrl=http://localhost:5173
reset.password.url=http://localhost:5173/resetowanie-hasla

````

## 3. Run application
Once your `application.properties` is configured, run the backend with Maven:

```bash
./mvnw clean install
./mvnw spring-boot:run
```
The backend will start on the default port 8080. You can access API endpoints at:

```
http://localhost:8080
```

