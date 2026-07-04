# SAWARI — Backend

REST API for SAWARI, a women-safety-first ride-sharing web application.

This repository contains the Spring Boot backend for SAWARI. The React.js frontend lives in [sawari-frontend](https://github.com/Aaliya005/sawari-frontend).

---

## Tech Stack

| Technology | Purpose |
|---|---|
| Spring Boot 3 | REST API framework |
| Spring Data JPA | Database ORM |
| Spring Security | Security configuration |
| MySQL | Relational database |
| BCrypt | Password hashing |
| Lombok | Boilerplate reduction |
| Multipart File Upload | Document storage |

---

## Project Structure

```
src/main/java/com/sawari/backend/
├── config/
│   ├── SecurityConfig.java
│   └── WebMvcConfig.java
├── controller/
│   ├── AuthController.java
│   ├── RideController.java
│   ├── BookingController.java
│   ├── SosAlertController.java
│   ├── DriverVerificationController.java
│   └── UserVerificationController.java
├── dto/
│   ├── LoginRequest.java
│   ├── LoginResponse.java
│   └── RegisterRequest.java
├── entity/
│   ├── User.java
│   ├── Ride.java
│   ├── Booking.java
│   ├── SosAlert.java
│   ├── DriverVerification.java
│   └── UserVerification.java
├── repository/
└── service/
    ├── UserService.java
    └── FileStorageService.java
```

---

## API Endpoints

### Auth — `/api/auth`
| Method | Endpoint | Description |
|---|---|---|
| POST | `/register` | Register a new user |
| POST | `/login` | Login and get user info |

### Rides — `/api/rides`
| Method | Endpoint | Description |
|---|---|---|
| POST | `/` | Driver creates a ride |
| GET | `/` | Get all active rides |
| GET | `/driver/{driverId}` | Driver's own rides |
| GET | `/{id}` | Get ride by ID |
| GET | `/admin/all` | Admin — all rides |
| PUT | `/{id}/cancel` | Cancel a ride |
| PUT | `/{id}/complete` | Complete a ride |

### Bookings — `/api/bookings`
| Method | Endpoint | Description |
|---|---|---|
| POST | `/` | Passenger books a ride |
| GET | `/passenger/{passengerId}` | Passenger's bookings |
| GET | `/ride/{rideId}` | Bookings for a ride |
| PUT | `/{id}/cancel` | Cancel a booking |

### SOS Alerts — `/api/sos`
| Method | Endpoint | Description |
|---|---|---|
| POST | `/` | Trigger SOS alert |
| GET | `/user/{userId}` | User's SOS history |
| GET | `/admin/all` | Admin — all alerts |
| PUT | `/{id}/resolve` | Resolve an alert |

### Driver Verification — `/api/verification`
| Method | Endpoint | Description |
|---|---|---|
| POST | `/` | Submit verification documents |
| GET | `/driver/{driverId}` | Get driver's verification status |
| GET | `/admin/all` | Admin — all submissions |
| PUT | `/{id}/approve` | Admin approves |
| PUT | `/{id}/reject` | Admin rejects |

### User Verification — `/api/user-verification`
| Method | Endpoint | Description |
|---|---|---|
| POST | `/` | Submit verification documents |
| GET | `/user/{userId}` | Get user's verification status |
| GET | `/admin/all` | Admin — all submissions |
| PUT | `/{id}/approve` | Admin approves |
| PUT | `/{id}/reject` | Admin rejects |

---

## Database Setup

Create a MySQL database:

```sql
CREATE DATABASE sawari_db;
```

Set admin role manually after registering:

```sql
UPDATE users SET role = 'ADMIN' WHERE email = 'your-admin@email.com';
```

---

## Getting Started

### Prerequisites
- Java 17+
- Maven
- MySQL running locally

### Configuration

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/sawari_db
spring.datasource.username=your_mysql_username
spring.datasource.password=your_mysql_password
spring.jpa.hibernate.ddl-auto=update
```

### Run

```bash
git clone https://github.com/Aaliya005/sawari-backend.git
cd sawari-backend
./mvnw spring-boot:run
```

API runs at **http://localhost:8080**

Uploaded files are served at `http://localhost:8080/uploads/{subfolder}/{filename}`

---

## Frontend

This backend powers the SAWARI React frontend.
See [sawari-frontend](https://github.com/Aaliya005/sawari-frontend) for setup instructions.

---

## Developer

**Aaliya Manyar**
GitHub: [Aaliya005](https://github.com/Aaliya005)