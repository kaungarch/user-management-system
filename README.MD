# User Management System

This is a User Management System built with Spring Boot and MySQL. It provides RESTful APIs for managing requests,
users, projects, black lists.

## Setup Instructions

### Prerequisites

- Java 17 or later
- Maven 3.8+
- MySQL database
- Docker

### Installation

1. **Clone the repository:**
   ```sh
   git clone <repository-url>
   cd user-management-system
   ```

2. **Configure the database:**
    - Update `application.properties` with your MySQL credentials:
    ```properties
       spring.datasource.url=jdbc:mysql://localhost:3306/user-management-db
       spring.datasource.username=root
       spring.datasource.password=root
       spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
       spring.sql.init.platform=mysql
       spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
       spring.jpa.hibernate.ddl-auto=update
    ```

3. **JWT Configuration:**
    - In the `application.properties` file, configure JWT settings as follows:
   ```properties
      jwt.secret=gR75cfmwphskYQ0YimpZ+mDQnZftDVeRGJZwn+xRsSk=
      jwt.issuer=http://localhost:8080
      jwt.accessToken.expiration=3600000
      jwt.refreshToken.expiration=259200000
   ```

4. **Build the project:**
   ```sh
   mvn clean install
   ```

5. **Run using Docker:**
   ```sh
   docker-compose up -d
   ```

6. **Run the application:**
   ```sh
   mvn spring-boot:run
   ```

## API Documentation in Swagger UI

This application includes API documentation using Swagger UI.

### Customizing API Docs

You can customize the API documentation by configuring application properties:

```
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.api-docs.enabled=true
springdoc.swagger-ui.enabled=true
```

### Accessing Swagger UI

After starting the application, you can access the Swagger UI at:

```
http://localhost:8080/api/swagger-ui/index.html
```

## API Documentation

### Base URL

```
http://localhost:8080/api/
```

### Request Endpoints (Account Registration Request)

#### 1. Create an Account Registration Request (Anyone can access)

**POST** `/api/requests`

##### Request Body:

```json
{
  "name": "John Doe",
  "nrcNumber": "10/thaphaya(N)001134",
  "phoneNumber": "959xxxxxxxxx",
  "password": "123456"
}
```

##### Response:

```json
{
  "id": 1,
  "name": "John Doe",
  "nrcNumber": "10/thaphaya(N)001134",
  "phoneNumber": "959xxxxxxxxx",
  "status": "PENDING"
}
```

#### 2. Check Registration Request Status (Anyone can access)

**GET** `/api/requests/status`

##### Query Parameters:

- `phoneNumber` - String phone number

##### Example Request #####

``` 
GET /api/requests/status?phoneNumber=959xxxxxxxxx
```

##### Response:

```json
{
  "data": "PENDING",
  "message": "your request is successful."
}
```

The `data` field can have one of the following values:

- `PENDING`
- `APPROVED`
- `REJECTED`
- `BLOCKED`
- `BLACKLISTED`

#### 3. Get all request list (Only admin can access)

**GET** `/api/requests`

##### Response:

```json
{
  "data": [
    {
      "id": 1,
      "name": "bob",
      "phoneNumber": "959111111111",
      "nrcNumber": "10/thaphaya(N)001134",
      "status": "APPROVED"
    },
    {
      "id": 5,
      "name": "john doe",
      "phoneNumber": "959222222222",
      "nrcNumber": "10/thaphaya(N)000002",
      "status": "REJECTED"
    }
  ],
  "message": "your request is successful."
}
```

#### 4. Get a Request by ID (Only admin can access)

**GET** `/api/requests/{id}`

#### Path Variable

- id - int ID

##### Example Request #####

```
GET /api/requests/1
```

##### Response:

```json
{
  "data": {
    "id": 1,
    "name": "john doe",
    "phoneNumber": "959xxxxxxxxx",
    "nrcNumber": "10/thaphaya(N)001134",
    "status": "REJECTED"
  },
  "message": "your request is successful."
}
```

#### 5. Approve a Request (Only admin can access)

**PUT** `/api/requests/{id}/approve`

#### Path Variable

- id - int ID

##### Example Request #####

``` 
PUT /api/requests/1/approve
```

##### Response:

```json
{
  "data": {
    "id": 1,
    "name": "john doe",
    "phoneNumber": "959xxxxxxxxx",
    "nrcNumber": "10/thaphaya(N)001134",
    "status": "APPROVED"
  },
  "message": "your request is successful."
}
```

#### 6. Reject a Request (Only admin can access)

**PUT** `/api/requests/{id}/reject`

#### Path Variable

- id - int ID

##### Example Request #####

```
PUT /api/requests/1/reject
```

##### Response:

```json
{
  "data": {
    "id": 1,
    "name": "john doe",
    "phoneNumber": "959xxxxxxxxx",
    "nrcNumber": "10/thaphaya(N)001134",
    "status": "REJECTED"
  },
  "message": "your request is successful."
}
```

#### 7. Block a Request (Only admin can access)

**PUT** `/api/requests/{id}/block`

#### Path Variable

- id - int ID

##### Example Request #####

```
PUT api/requests/1/block
```

##### Response:

```json
{
  "data": {
    "id": 1,
    "name": "john doe",
    "phoneNumber": "959xxxxxxxxx",
    "nrcNumber": "10/thaphaya(N)001134",
    "status": "BLOCKED"
  },
  "message": "your request is successful."
}
```

#### 8. Unblock a Request (Only admin can access)

**PUT** `/api/requests/{id}/unblock`

#### Path Variable

- id - int ID

##### Example Request #####

```
PUT api/requests/1/unblock
```

##### Response:

```json
{
  "data": {
    "id": 1,
    "name": "john doe",
    "phoneNumber": "959xxxxxxxxx",
    "nrcNumber": "10/thaphaya(N)001134",
    "status": "PENDING"
  },
  "message": "your request is successful."
}
```

---

### Authentication Endpoints

#### 1. Sign in to Account (Anyone can access)

**POST** `/api/auth/signin`

##### Request Body:

```json
{
  "phoneNumber": "959xxxxxxxxx",
  "password": "123456"
}
```

##### Response:

```json
{
  "status": "success",
  "message": "Sign in successful."
}
```

#### 2. Refresh Access Token (Anyone can access)

**POST** `/api/auth/refresh`

##### Response:

```json
{
  "status": "success",
  "message": "Refresh token successfully."
}
```

#### 3. Sign out (Authenticated users can access)

**POST** `/api/auth/signout`

##### Response:

```json
{
  "status": "success",
  "message": "Refresh token successfully."
}
```

---

### User Endpoints

#### 1. Get all Users List (Only admin can access)

**GET** `/api/users`

##### Query Parameters:

- `name` - Filter by name (Optional)
- `phoneNumber` - Filter by phone number (Optional)
- `nrcNumber` - Filter by nrc number (Optional)
- `role` - Filter by role (Optional)

##### Example Request #####

```
GET /api/users?role=admin
```

##### Response:

```json
{
  "data": [
    {
      "id": "c3a36508-4715-4779-b540-47045e8d90cf",
      "name": "kaung",
      "nrcNumber": "10/thaphaya(N)123456",
      "phoneNumber": "959973468287",
      "role": "SUPER_ADMIN"
    }
  ],
  "message": "your request is successful."
}
```

#### 2. Get a User by ID (Only admin can access)

**GET** `/api/users/{id}`

##### Path Variable:

- id - UUID

##### Example Request #####

```
GET /api/users/xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx
```

##### Response:

```json
{
  "data": {
    "id": "0ff62013-1565-4d5e-b01f-e1be1f4ab957",
    "name": "john doe",
    "nrcNumber": "10/thaphaya(N)001135",
    "phoneNumber": "959xxxxxxxxx",
    "role": "USER"
  },
  "message": "your request is successful."
}
```

---

### Project Endpoints

#### 1. Create a Project (Only authenticated system users can access)

**POST** `/api/projects`

##### Request Body:

```json
{
  "title": "company's project",
  "description": "about our project"
}
```

##### Response:

```json
{
  "data": {
    "id": 7,
    "title": "company's project",
    "description": "about our project"
  },
  "message": "your request is successful."
}
```

#### 2. Update Existing Project (Only authenticated system users can access)

**PUT** `/api/projects/{id}`

##### Path Variable:

- id - int ID

#### Request Body:

```json
{
  "title": null,
  "description": "changed description"
}
```

##### Response:

```json
{
  "data": {
    "id": 1,
    "title": "company's project",
    "description": "changed description"
  },
  "message": "your request is successful."
}
```

#### 3. Soft Delete Project (Only authenticated system users can access)

**PUT** `/api/projects/{id}/soft-delete`

##### Path Variable

- id - int ID

##### Example Request:

```
PUT /api/projects/1/soft-delete
```

##### Response:

```json
{
  "data": null,
  "message": "your request is successful."
}
```

#### 4. Undo Project Deletion (Only authenticated system users can access)

**PUT** `/api/projects/{id}/undelete`

##### Path Variable

- id - int ID

##### Example Request:

```
PUT /api/projects/1/undelete
```

##### Response:

```json
{
  "data": null,
  "message": "your request is successful."
}
```

#### 5. Delete Project Permanently (Only authenticated system users can access)

**DELETE** `/api/projects/{id}`

##### Path Variable

- id - int ID

##### Example Request:

```
DELETE /api/projects/1
```

##### Response:

```json
{
  "data": null,
  "message": "your request is successful."
}
```

#### 6. Get all Projects List (Both admin and authenticated system users can access)

**GET** `/api/projects`

##### Query Parameters #####

- `title` - Filter by project title (Optional)
- `deleted` - Filter by project status (`null`|`true`|`false`) (Optional)
- `createdAfter` - Filter by created time (Optional)
- `createdBefore` - Filter by created time (Optional)

##### Example Request #####

```
GET /api/projects?title=project&createdAt=2025-05-12
```

##### Response:

```json
{
  "data": [
    {
      "id": 1,
      "title": "project 1.1",
      "description": "about our project"
    },
    {
      "id": 2,
      "title": "project 2.1",
      "description": "changed this"
    }
  ],
  "message": "your request is successful."
}
```

---

### Black List Endpoints

#### 1. Blacklist a Request (Only admin can access)

**POST** `/api/blacklists`

##### Request Body

```json
{
  "requestId": 1
}
```

##### Response:

```json
{
  "data": {
    "id": 2,
    "nrcNumber": "10/thaphaya(N)444444",
    "phoneNumber": "959xxxxxxxxx"
  },
  "message": "your request is successful."
}
```

#### 2. Delete a BlackList (Only admin can access)

**DELETE** `/api/blacklists/{id}`

##### Path Variable:

- id - int ID

##### Example Request #####

```
DELETE /api/blacklists/1
```

##### Response:

```json
{
  "data": null,
  "message": "your request is successful."
}
```

---

## License

This project is for educational purposes as part of an internship assignment.

