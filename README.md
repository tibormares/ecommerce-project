# Spring Boot eCommerce API

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.java.com)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Database](https://img.shields.io/badge/Database-PostgreSQL-blue.svg)](https://www.postgresql.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

A backend for an e-commerce platform built with Java and Spring Boot. This project demonstrates a complete RESTful API with JWT-based security for managing users, addresses, products, categories, carts and orders.

> **Note:** The live demo on AWS is currently offline (project archived). Please refer to the "Run the Project Locally" section to test the application.

---

## ✨ Features

* **User Authentication:** Secure registration, login, and logout endpoints using **Spring Security** and **JWT**.
* **Role-Based Access Control:** Clear distinction between `USER` and `ADMIN` roles for accessing different endpoints.
* **Product & Category Management:** Full CRUD (Create, Read, Update, Delete) operations for products and categories.
* **Shopping Cart:** Functionality for authenticated users to add, update, and remove products from their personal cart.
* **Address Management:** Users can manage multiple personal addresses for shipping.
* **Order Placement:** A streamlined process for creating an order from the contents of the shopping cart.
* **Interactive API Documentation:** A live **Swagger UI** endpoint for easy exploration and testing of all API functionalities.

---

## 🛠️ Tech Stack

* **Framework:** Spring Boot (Web, Security, Data JPA)
* **Language:** Java 21
* **Database:** PostgreSQL (Production), H2 In-Memory (Development & Testing)
* **Security:** JSON Web Tokens (JWT)
* **Build Tool:** Apache Maven
* **Testing:** JUnit, Mockito
* **Utilities:** Lombok, ModelMapper
* **API Documentation:** OpenAPI (SpringDoc)

---
## 🚀 DevOps / CI/CD

This project was configured for continuous deployment to the cloud.

* **CI/CD Platform:** GitHub Actions (automated build, testing, and deployment)
* **Deployment Platform:** AWS Elastic Beanstalk (hosted the application environment)
* **Production Database:** AWS RDS (PostgreSQL)

---

## 🚀 Getting Started

To explore and test this project, please run it locally on your machine.

### Run the Project Locally 💻

Follow these steps using IntelliJ IDEA or any IDE of your choice.

#### Prerequisites

* Git
* JDK 21 or newer
* Apache Maven (usually bundled with IntelliJ)

#### Installation & Execution

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/tibormares/ecommerce-project.git
    ```

2.  **Open in IntelliJ IDEA:**
    * Open IntelliJ IDEA and select `File -> Open...` and navigate to the cloned project folder.


3.  **Run the application:**
    * Navigate to the main application class located at `src/main/java/com/ecommerce/project/SbEcomApplication.java`.
    * Click the green "play" icon ▶️ next to the `main` method to start the application.
    * The application will be available at `http://localhost:5000`. You can now send requests to the API.


The project is configured with a `dev` profile that runs out-of-the-box with an H2 in-memory database, requiring no extra setup.

> **Explore the Database:** While the application is running locally, you can view the H2 database console in your browser at http://localhost:5000/h2-console/.  
> Use the JDBC URL `jdbc:h2:mem:test`, username `sa`, and password `password` to log in.

## 🧑‍🤝‍🧑 Default Users (for Local Setup)

When running in the `dev` profile (the default for local execution), the following test users are automatically created in the database, including a full-access admin.

| Role      | Username | Password |
|-----------|----------|----------|
| **Admin** | `admin1` | `admin1` |
| **User**  | `user1`  | `user1`  |

**Note:** These accounts are only created when running the project locally. To test the deployed version, please use the `user1` demo account mentioned above.

## 🛜 API endpoints
![Swagger UI Overview](swagger-overview.png)
