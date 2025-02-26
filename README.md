# User Authentication & Role-Based API Service

## Overview
This project is a **Java-based REST API** built with **Spring Boot**, providing **user authentication (Sign Up/Sign In)** and **role-based access control**. The system uses **MySQL** for data storage and differentiates between three roles:  
- **Super Admin**: Full access to create, update, and delete users.  
- **Admin**: Can view all users and create new users.  
- **User**: Can only view their own profile.  

## 🛠️ Tech Stack
- **Language:** Java  
- **Framework:** Spring Boot  
- **Security:** Spring Security (with JWT authentication)  
- **Database:** MySQL  
- **Build Tool:** Maven

## ⚙️ Features  
### ✅ User Authentication  
- **Sign Up Endpoint:**  
  - Accepts user registration details (username, password).  
  - Hashes passwords securely (BCrypt).  
  - Assigns the default role of "User".  
- **Sign In Endpoint:**  
  - Authenticates users based on credentials.  
  - Issues a JWT token for stateless authentication.  

### ✅ Role-Based Access Control  
- **Super Admin:** Full control over users.  
- **Admin:** Can view all users and create new users.  
- **User:** Can only access their own profile.  

## 📖 API Endpoints  
### 🔹 Authentication APIs  
| Method | Endpoint | Description |
|--------|---------|-------------|
| `POST` | `/register` | Register a new user |
| `POST` | `/login` | Authenticate user & return JWT token |

### 🔹 User-Specific APIs (`/user`)  
| Method | Endpoint | Description |
|--------|---------|-------------|
| `GET`  | `/user/profile` | Fetch logged-in user's profile |

### 🔹 Admin APIs (`/admin`)  
| Method | Endpoint | Description |
|--------|---------|-------------|
| `GET`  | `/admin/users` | Get a list of all users |
| `GET`  | `/admin/user/{id}` | Fetch a specific user by ID |
| `POST` | `/admin/create-user` | Create a new user (Admin only) |

### 🔹 Super Admin APIs (`/super-admin`)  
| Method | Endpoint | Description |
|--------|---------|-------------|
| `GET`  | `/super-admin/users` | Get a list of all users |
| `GET`  | `/super-admin/user/{id}` | Fetch a specific user by ID |
| `POST` | `/super-admin/create-user` | Create a new user |
| `PUT`  | `/super-admin/update-user/{id}` | Update a user |
| `DELETE` | `/super-admin/delete-user/{id}` | Delete a user |


## 🏗️ Project Setup  

### 📥 Import Project into Eclipse  
1. Open **Eclipse IDE**  
2. Click on **File** → **Import**  
3. Select **Maven** → **Existing Maven Projects**  
4. Click **Next**  
5. Browse to the cloned project directory and click **Finish**  

### 🛠️ Configure MySQL Database  

1. **Start MySQL Server**  
   Ensure MySQL is installed and running on your machine.  

2. **Create a New Database**  
   Open MySQL CLI or any database client (e.g., MySQL Workbench) and run:  
   ```sql
   CREATE DATABASE your_database;

### 🛠️ Configure application properties  
# Database Configuration  
spring.datasource.url=jdbc:mysql://localhost:3306/your_database
spring.datasource.username=root
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver  
spring.jpa.hibernate.ddl-auto=update
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect

# Server Configuration  
server.port=8080

### 🛠️ check pom.xml added dependencies 
<dependencies>
    <!-- Spring Boot Starter Web -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <!-- Spring Boot Starter Security -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>

    <!-- Spring Boot Starter Data JPA -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>

    <!-- MySQL Driver -->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
    </dependency>

    <!-- JWT Authentication -->
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt</artifactId>
        <version>0.11.5</version>
    </dependency>

   



### 1️⃣ Clone the Repository  
```sh
git clone https://github.com/tabrezklizos/rbac_project.git
cd your-repo


