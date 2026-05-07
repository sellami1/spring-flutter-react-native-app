# Auth Service

Authentication microservice built with Express, MongoDB, and JWT.

## Features

- User registration with bcrypt password hashing
- User login with JWT token generation
- MongoDB persistence with Mongoose
- CORS support
- Error handling middleware
- Health check endpoint

## Prerequisites

- Node.js 18+
- MongoDB 5.0+

## Installation

1. Install dependencies:
```bash
npm install
```

2. Create `.env` file (see `.env.example`):
```bash
cp .env.example .env
```

3. Update environment variables:
```env
PORT=3001
MONGO_URI=mongodb://localhost:27017/auth_db
JWT_SECRET=your-secret-key-change-in-production
NODE_ENV=development
```

## Running Locally

```bash
# Production
npm start

# Development (with nodemon)
npm run dev
```

The service will be available at `http://localhost:3001`.

## API Endpoints

### Health Check
```bash
GET /health
```

### Register
```bash
POST /auth/register
Content-Type: application/json

{
  "username": "john_doe",
  "password": "securePassword123"
}
```

Response (201):
```json
{
  "id": "64f5a1b2c3d4e5f6g7h8i9j0",
  "username": "john_doe",
  "message": "User registered successfully"
}
```

### Login
```bash
POST /auth/login
Content-Type: application/json

{
  "username": "john_doe",
  "password": "securePassword123"
}
```

Response (200):
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": "64f5a1b2c3d4e5f6g7h8i9j0",
    "username": "john_doe"
  },
  "message": "Login successful"
}
```

## Docker

Build and run with Docker:

```bash
# Build image
docker build -t auth-service .

# Run container
docker run -p 3001:3001 \
  -e MONGO_URI=mongodb://host.docker.internal:27017/auth_db \
  -e JWT_SECRET=your-secret-key \
  auth-service
```

## Docker Compose

The service is integrated into the main `docker-compose.yml`:

```bash
docker compose up auth-service mongodb
```

## Security Notes

- **JWT_SECRET**: Change in production! Use a strong, random string.
- **Password hashing**: Bcrypt with 10 rounds (configurable in auth.js).
- **Token expiry**: 1 hour by default (configurable in auth.js).
- **CORS**: Currently allows all origins (customize in app.js for production).

## Error Handling

- **400**: Missing or invalid request data
- **401**: Invalid credentials or token
- **409**: Username already exists
- **500**: Server error

## Development

Ensure code quality:
- Input validation on all endpoints
- Proper error messages (avoid exposing implementation details)
- Logging for debugging
- MongoDB index on unique username field

## License

MIT
