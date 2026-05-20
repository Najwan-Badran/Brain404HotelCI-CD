# Brain404 Hotel Management System

A comprehensive hotel management REST API built with Spring Boot 3.2.5, featuring JWT authentication, booking management, payment processing, room management, reviews, notifications, and analytics.

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Architecture](#architecture)
- [Modules](#modules)
- [Prerequisites](#prerequisites)
- [Environment Configuration](#environment-configuration)
- [Installation](#installation)
- [API Documentation](#api-documentation)
- [Security](#security)
- [Testing](#testing)
- [Project Structure](#project-structure)
- [Error Handling](#error-handling)
- [Pagination](#pagination)
- [Contributing](#contributing)
- [License](#license)

## Overview

Brain404 Hotel is a full-featured hotel management backend system that provides RESTful APIs for managing hotels, rooms, bookings, payments, reviews, and user accounts. The system implements role-based access control with JWT authentication, supports multiple payment methods, and includes real-time notification capabilities.

## Features

### Authentication & Authorization
- JWT-based authentication with access and refresh tokens
- Role-based access control (ADMIN, MANAGER, RECEPTIONIST, USER)
- Account lockout mechanism after failed login attempts
- BCrypt password encryption
- Token refresh functionality

### Hotel Management
- CRUD operations for hotels
- Star rating system (1-5)
- Amenity management (pool, spa, gym, wifi, etc.)
- Hotel search by city, country, and star rating
- Image management for hotels

### Room Management
- Room CRUD operations with room types
- Room availability tracking
- Capacity and pricing configuration
- Advanced room search with filters:
  - Hotel ID
  - Check-in/Check-out dates
  - Number of guests
  - Price range (min/max)
  - Room type name
  - Amenities (WiFi, Pool, etc.)

### Booking System
- Complete booking lifecycle management
- Status tracking (PENDING, CONFIRMED, CHECKED_IN, CHECKED_OUT, CANCELLED, NO_SHOW)
- Guest information management
- Date range validation
- Booking cancellation with policy enforcement
- Check-in and check-out processing

### Payment Processing
- Multiple payment method support
- Payment status tracking (PENDING, PAID, REFUNDED, CANCELLED)
- Partial payment support
- Refund processing
- Payment transaction history

### Reviews & Ratings
- User review submission
- Rating system (1-5 stars)
- Review approval workflow
- Hotel response to reviews
- Average rating calculation

### Notifications
- User notification system
- Read/unread status tracking
- Automatic notifications for booking events

### Analytics & Statistics
- Hotel performance statistics
- Booking analytics
- Revenue tracking

## Tech Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 21 | Programming Language |
| Spring Boot | 3.2.5 | Application Framework |
| Spring Security | 6.x | Authentication & Authorization |
| Spring Data JPA | 3.x | Data Persistence |
| MySQL | 8.x | Production Database |
| H2 Database | 2.x | Test Database |
| JWT (jjwt) | 0.12.5 | Token Management |
| Lombok | 1.18.x | Boilerplate Reduction |
| MapStruct | 1.5.5 | Object Mapping |
| Springdoc OpenAPI | 2.5.0 | API Documentation |
| JUnit 5 | 5.x | Unit Testing |
| Mockito | 5.x | Mocking Framework |

## Architecture

The application follows a layered architecture pattern:

```
+-------------------------------------------------------------+
|                      Controllers                             |
|  (REST API endpoints, request/response handling)            |
+-------------------------------------------------------------+
|                       Services                               |
|  (Business logic, transaction management)                   |
+-------------------------------------------------------------+
|                      Repositories                            |
|  (Data access, JPA queries)                                 |
+-------------------------------------------------------------+
|                       Entities                               |
|  (Domain models, JPA entities)                              |
+-------------------------------------------------------------+
|                       Database                               |
|  (MySQL / H2)                                               |
+-------------------------------------------------------------+
```

### Cross-Cutting Concerns
- **Security**: JWT filter chain, authentication/authorization
- **Exception Handling**: Global exception handler with standardized error responses
- **Validation**: Custom validators for passwords, date ranges, etc.
- **Mapping**: DTO to Entity conversion using MapStruct

## Modules

| Module | Description |
|--------|-------------|
| **User** | User registration, profile management, role assignment |
| **Security** | JWT authentication, token generation/validation, refresh tokens |
| **Role** | Role management (ADMIN, MANAGER, RECEPTIONIST, USER) |
| **Hotel** | Hotel CRUD, amenity management, search functionality |
| **Room** | Room management, availability tracking |
| **RoomType** | Room type configuration (capacity, pricing) |
| **RoomAvailability** | Room availability scheduling |
| **Booking** | Booking lifecycle, status management, guest handling |
| **BookingGuest** | Guest information for bookings |
| **Payment** | Payment processing, status tracking |
| **PaymentTransaction** | Transaction history and records |
| **Review** | User reviews, ratings, approval workflow |
| **Notification** | User notifications, read status |
| **Amenity** | Hotel and room amenities |
| **Image** | Image management for hotels/rooms |
| **Address** | Address handling for hotels |
| **Stats** | Analytics and statistics |

## Prerequisites

- **Java 21** or higher
- **Maven 3.8+**
- **MySQL 8.0+** (for production)
- **Git**

## Environment Configuration

Create environment variables or update `application.properties`:

### Required Environment Variables

| Variable | Description | Example |
|----------|-------------|---------|
| `DB_PASSWORD` | MySQL database password | `123456` |
| `JWT_SECRET` | JWT signing key (Base64 encoded, min 256 bits) | `YWJjZGVmZ2hpamts...` |

### Application Properties

```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/brain404hotel
spring.datasource.username=root
spring.datasource.password=${DB_PASSWORD:123456}

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=true

# JWT Configuration
jwt.secret=${JWT_SECRET}
jwt.expiration=900000
jwt.refresh-expiration=604800000

# Server Configuration
server.port=8080
```

## Installation

### 1. Clone the Repository

```bash
git clone https://github.com/yourusername/brain404hotel.git
cd brain404hotel
```

### 2. Configure Database

Create a MySQL database:

```sql
CREATE DATABASE brain404hotel;
CREATE USER 'hotel_user'@'localhost' IDENTIFIED BY '123456';
GRANT ALL PRIVILEGES ON brain404hotel.* TO 'hotel_user'@'localhost';
FLUSH PRIVILEGES;
```

### 3. Set Environment Variables

**Linux/macOS:**
```bash
export DB_PASSWORD=123456
export JWT_SECRET=your_base64_encoded_secret_key
```

**Windows:**
```cmd
set DB_PASSWORD=123456
set JWT_SECRET=your_base64_encoded_secret_key
```

### 4. Build the Application

```bash
mvn clean install
```

### 5. Run the Application

**Empty Mode (default)** - Only schema, no data:
```bash
mvn spring-boot:run
```

**Demo Mode** - Schema with realistic fake data:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=demo
```

Or set environment variable:
```bash
# Linux/macOS
export SPRING_PROFILES_ACTIVE=demo

# Windows
set SPRING_PROFILES_ACTIVE=demo
```

The application will start on `http://localhost:8080`

### Database Initialization Profiles

| Profile | Description |
|---------|-------------|
| `empty` (default) | Creates schema only, no seed data. Admin user created on first run. |
| `demo` | Creates schema + realistic demo data (hotels, rooms, bookings, users, etc.) |
| `test` | H2 in-memory database for testing |

### Default Admin Credentials

On first run, an admin user is automatically created:

| Field | Value |
|-------|-------|
| Username | `admin` |
| Password | `Admin@1234` |
| Email | `admin@hotel.com` |
| Roles | ADMIN, USER, MANAGER |

**Important:** Change the admin password after first login in production!

### Demo Data (when using `demo` profile)

The demo profile seeds the database with realistic test data:

| Entity | Count | Description |
|--------|-------|-------------|
| Addresses | 25 | 10 hotel addresses, 15 user billing addresses |
| Amenities | 20 | WiFi, Pool, Spa, Gym, Restaurant, etc. |
| Room Types | 10 | Standard Single to Penthouse |
| Hotels | 10 | Worldwide locations (NYC, Miami, London, Tokyo, etc.) |
| Rooms | ~440 | Distributed across all hotels |
| Users | 15 | Demo users with ROLE_USER |
| Bookings | 50 | Various statuses (confirmed, pending, cancelled) |
| Booking Guests | ~100 | Guest details for bookings |
| Payments | 50 | One per booking |
| Reviews | ~35 | User reviews for hotels |

Demo users have the password: `Password@123`

## API Documentation

### Swagger UI

Access the interactive API documentation at:
```
http://localhost:8080/swagger-ui.html
```

### Postman Collection

A comprehensive Postman collection is included in the project root:

```
Brain404HotelPostman.json
```

**Features:**
- All 16 modules with complete endpoint coverage
- Success and error scenario folders for each module
- Auto-save tokens on login via test scripts
- Collection variables for `baseUrl`, `accessToken`, `refreshToken`

**To use:**
1. Import `Brain404HotelPostman.json` into Postman
2. Run "Login - Admin" first to populate the access token
3. All subsequent requests will use the saved token

### API Endpoints Overview

| Controller | Base Path | Description |
|------------|-----------|-------------|
| AuthController | `/api/auth` | Authentication (login, register, refresh, logout, password reset) |
| UserController | `/users` | User management, profile, bookings, reviews |
| HotelController | `/hotels` | Hotel CRUD, search, amenities |
| RoomController | `/rooms` | Room management, search, availability |
| RoomTypeController | `/room-types` | Room type configuration |
| BookingController | `/bookings` | Booking lifecycle (confirm, check-in/out, cancel) |
| BookingGuestController | `/bookings/{id}/guests` | Guest management for bookings |
| PaymentController | `/payments` | Payment processing, refunds |
| ReviewController | `/reviews` | Reviews, ratings, approval workflow |
| NotificationController | `/notifications` | User notifications, read status |
| AmenityController | `/amenities` | Amenity management |
| RoleController | `/role` | Role management (Admin only) |
| StatsController | `/stats` | Analytics and dashboard |
| AddressController | `/api/addresses` | Address management, search |
| ImageController | `/images` | Image management |
| RoomAvailabilityController | `/room-availability` | Availability scheduling |

### Authentication Endpoints

```
POST /api/auth/login          - User login
POST /api/auth/register       - User registration
POST /api/auth/refresh        - Refresh access token
POST /api/auth/logout         - User logout
```

### Sample API Requests

**Login:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "user@example.com", "password": "Password123!"}'
```

**Create Hotel (Admin):**
```bash
curl -X POST http://localhost:8080/api/hotels \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Grand Hotel",
    "city": "New York",
    "country": "USA",
    "starRating": 5
  }'
```

**Search Hotels:**
```bash
curl "http://localhost:8080/api/hotels/search?city=New%20York&minStars=4&page=0&size=10"
```

**Search Rooms with Filters:**
```bash
curl "http://localhost:8080/rooms/search?hotelId=1&checkInDate=2024-06-01&checkOutDate=2024-06-05&guests=2&minPrice=100&maxPrice=300&roomTypeName=Deluxe&amenity=WiFi"
```

**Create Booking:**
```bash
curl -X POST http://localhost:8080/api/bookings \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "roomId": 1,
    "checkInDate": "2024-06-01",
    "checkOutDate": "2024-06-05",
    "numberOfGuests": 2,
    "numberOfAdults": 2
  }'
```

**Create Address (Admin):**
```bash
curl -X POST http://localhost:8080/api/addresses \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "addressType": "HOTEL",
    "street": "123 Main Street",
    "street2": "Suite 100",
    "city": "New York",
    "state": "NY",
    "country": "USA",
    "postalCode": "10001",
    "latitude": 40.7128,
    "longitude": -74.0060
  }'
```

**Search Addresses:**
```bash
curl "http://localhost:8080/api/addresses/search?city=New%20York&country=USA"
```

**Get Addresses by Type:**
```bash
curl "http://localhost:8080/api/addresses/type/HOTEL"
```

## Security

### Authentication Flow

1. User submits credentials to `/api/auth/login`
2. Server validates credentials and returns JWT access token and refresh token
3. Client includes access token in `Authorization: Bearer <token>` header
4. Server validates token on each request via `JwtAuthFilter`
5. When access token expires, client uses refresh token to obtain new access token

### Public Endpoints

The following endpoints are accessible without authentication:
- `POST /api/auth/login`
- `POST /api/auth/register`
- `POST /api/auth/refresh`
- `GET /api/hotels/**` (public hotel browsing)
- `GET /swagger-ui/**`
- `GET /v3/api-docs/**`

### Role Hierarchy

| Role | Permissions |
|------|-------------|
| ADMIN | Full system access |
| MANAGER | Hotel and staff management |
| RECEPTIONIST | Booking and guest management |
| USER | Personal bookings and reviews |

### Password Requirements

- Minimum 8 characters
- At least one uppercase letter
- At least one lowercase letter
- At least one digit
- At least one special character

## Testing

### Running Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=HotelServiceTest

# Run tests for a specific package
mvn test "-Dtest=com.Address.*"

# Run with coverage report
mvn test jacoco:report

# Clean build and test
mvn clean test
```

### Test Configuration

Tests use H2 in-memory database configured in `application-test.properties`:

```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.jpa.hibernate.ddl-auto=create-drop
```

### Test Categories

| Type | Annotation | Purpose |
|------|------------|---------|
| Unit Tests | `@ExtendWith(MockitoExtension.class)` | Service layer testing |
| Controller Tests | `@WebMvcTest` | REST endpoint testing |
| Repository Tests | `@DataJpaTest` | JPA query testing |
| Integration Tests | `@SpringBootTest` | Full context testing |

### Test Coverage

The test suite includes **457+ tests** covering:
- Service layer business logic
- Controller endpoints with MockMvc
- Repository custom queries
- Entity calculations and methods
- Custom validators
- Exception handling
- Security filters
- Database initialization profiles
- Address management operations
- Review statistics and admin functions
- Notification management
- Room availability operations

## Project Structure

```
src/
├── main/
│   ├── java/com/
│   │   ├── Address/           # Address entity and services
│   │   ├── Amenity/           # Amenity management
│   │   ├── Booking/           # Booking system
│   │   ├── BookingGuest/      # Guest information
│   │   ├── Common/            # Shared utilities
│   │   ├── Config/            # Application configuration
│   │   ├── Hotel/             # Hotel management
│   │   ├── Image/             # Image handling
│   │   ├── Notification/      # Notification system
│   │   ├── Payment/           # Payment processing
│   │   ├── PaymentTransaction/# Transaction records
│   │   ├── Review/            # Review system
│   │   ├── Role/              # Role management
│   │   ├── Room/              # Room management
│   │   ├── RoomAvailability/  # Availability tracking
│   │   ├── RoomType/          # Room type config
│   │   ├── Security/          # JWT and auth
│   │   ├── Stats/             # Analytics
│   │   ├── User/              # User management
│   │   ├── Validation/        # Custom validators
│   │   ├── ApiError.java      # Error response model
│   │   ├── GlobalExceptionHandler.java
│   │   └── Main.java          # Application entry point
│   └── resources/
│       ├── application.properties
│       └── application-test.properties
└── test/
    └── java/com/
        ├── Address/           # Address service tests
        ├── Booking/           # Booking tests
        ├── Hotel/             # Hotel tests
        ├── Security/          # Security tests
        ├── User/              # User tests
        ├── Validation/        # Validator tests
        ├── config/            # Profile and initialization tests
        └── GlobalExceptionHandlerTest.java
```

## Error Handling

The application uses a global exception handler that returns standardized error responses.

### Error Response Format

```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Hotel not found with id: 1",
  "path": "/api/hotels/1"
}
```

### HTTP Status Codes

| Status | Description | Example |
|--------|-------------|---------|
| 200 | Success | Resource retrieved |
| 201 | Created | Resource created |
| 400 | Bad Request | Validation error |
| 401 | Unauthorized | Invalid credentials |
| 403 | Forbidden | Insufficient permissions |
| 404 | Not Found | Resource not found |
| 409 | Conflict | Duplicate resource |
| 423 | Locked | Account locked |
| 500 | Internal Error | Server error |

### Custom Exceptions

- `UserNotFoundException`, `HotelNotFoundException`, `BookingNotFoundException`, `AddressNotFoundException`, etc.
- `UserAlreadyExistsException`, `HotelAlreadyExistsException`, etc.
- `BookingBadRequestException`, `RoleBadRequestException`, etc.
- `RefreshTokenException`
- `AccessDeniedException`

## Pagination

All list endpoints support pagination:

### Request Parameters

| Parameter | Default | Description |
|-----------|---------|-------------|
| `page` | 0 | Page number (0-indexed) |
| `size` | 10 | Items per page |
| `sort` | varies | Sort field and direction |

### Response Format

```json
{
  "content": ["..."],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10
  },
  "totalElements": 100,
  "totalPages": 10,
  "last": false,
  "first": true
}
```

### Example

```bash
GET /api/hotels?page=0&size=20&sort=name,asc
```

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Code Standards

- Follow Java naming conventions
- Write unit tests for new features
- Update documentation as needed
- Ensure all tests pass before submitting PR

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

**Brain404 Hotel Management System** - Built with Spring Boot
