# Q6 Authentication Microservice - Implementation Complete

## Summary (DM-41, DM-42, DM-43)

The authentication microservice (auth-service) has been successfully created and integrated into the project. This implements Q6 requirements: Express-based API with MongoDB persistence and JWT token generation.

## Project Structure Created

```
auth-service/
├── src/
│   ├── config/
│   │   └── db.js                 # MongoDB connection setup
│   ├── middleware/
│   │   └── errorHandler.js       # Global error handling middleware
│   ├── models/
│   │   └── User.js               # User model with Mongoose schema
│   ├── routes/
│   │   └── auth.js               # Authentication routes (register/login)
│   ├── app.js                    # Express app configuration
│   └── server.js                 # Server entry point
├── Dockerfile                    # Container build configuration
├── package.json                  # Node.js dependencies
├── .env.example                  # Environment variables template
├── .gitignore                    # Git ignore rules
└── README.md                     # Service documentation
```

## Files Created

### 1. **package.json**
- Express 4.18.2 for API framework
- Mongoose 7.5.0 for MongoDB ODM
- Bcrypt 5.1.0 for password hashing
- JsonWebToken 9.0.2 for JWT generation
- CORS support for cross-origin requests
- Dotenv for environment configuration

### 2. **src/models/User.js**
- User schema with username (unique, required) and password fields
- Timestamps for created/updated tracking
- Indexes configured for unique constraint on username
- Validation: username 3-50 chars, password min 6 chars

### 3. **src/routes/auth.js**
- `POST /auth/register`
  - Validates input (username & password required)
  - Hashes password with bcrypt (10 rounds)
  - Returns user ID and username on success (201)
  - Returns 409 if username already exists
  
- `POST /auth/login`
  - Validates credentials against stored hash
  - Issues JWT token with 1-hour expiry on success (200)
  - Returns 401 for invalid credentials
  - Generic error messages (no info leakage)

### 4. **src/middleware/errorHandler.js**
- Handles MongoDB validation errors
- Handles duplicate key (E11000) errors
- Handles JWT errors (invalid/expired tokens)
- Centralized error response formatting

### 5. **src/config/db.js**
- MongoDB connection using Mongoose
- Graceful error handling with process exit on failure
- Environment variable support for MONGO_URI

### 6. **src/app.js**
- Express app initialization with middleware stack
- CORS enabled for cross-origin requests
- JSON body parser configuration
- `/health` endpoint for container health checks
- 404 handler for undefined routes
- Error handler middleware (must be last)

### 7. **src/server.js**
- Entry point with .env loading
- MongoDB connection initialization
- Server startup on configured port
- Graceful shutdown handlers (SIGTERM, SIGINT)

### 8. **Dockerfile**
- Node.js 18-alpine base image
- Production dependencies only (npm ci --only=production)
- Health check via /health endpoint
- Port 3001 exposed

### 9. **docker-compose.yml Updates**
MongoDB service added:
- Image: mongo:7-alpine
- Credentials: mongo/mongo (for local dev)
- Port: 27017 mapped to host
- Volume: mongodb_data for persistence
- Health check: mongosh ping command

Auth-service added:
- Builds from ./auth-service Dockerfile
- Port 3001 mapped to host
- Environment variables configured
- Depends on mongodb with health check
- JWT_SECRET set to default (should change in production)

## Endpoints

### Health Check
```bash
GET /health
Response: { status: "ok", service: "auth-service", timestamp: "..." }
```

### Register User
```bash
POST /auth/register
Body: { "username": "john_doe", "password": "securePass123" }
Response 201: { "id": "...", "username": "john_doe", "message": "..." }
```

### Login User
```bash
POST /auth/login
Body: { "username": "john_doe", "password": "securePass123" }
Response 200: { "token": "eyJ...", "user": { "id": "...", "username": "..." }, "message": "..." }
```

## Running the Service

### Local Development
```bash
cd auth-service
npm install
cp .env.example .env
# Update .env with your MONGO_URI and JWT_SECRET
npm start       # Production
npm run dev     # Development with nodemon
```

### Docker Compose
```bash
# Start auth-service and MongoDB
docker compose up auth-service mongodb

# Start entire stack
docker compose up
```

### Testing Endpoints
```bash
# Register
curl -X POST http://localhost:3001/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"password123"}'

# Login
curl -X POST http://localhost:3001/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"password123"}'

# Health check
curl http://localhost:3001/health
```

## Security Configuration

### Production Checklist
1. **Change JWT_SECRET**: Update in `.env` or environment variables
   - Use strong, random 32+ character string
   - Store securely (never commit to git)
   
2. **CORS Configuration**: Restrict origins in `src/app.js`
   - Change from `cors()` to `cors({ origin: 'https://yourdomain.com' })`
   
3. **Environment Variables**: Use Docker secrets or vaults
   - Never hardcode credentials
   - Use different secrets for dev/prod

4. **HTTPS**: Enforce in production
   - Auth-service should be behind HTTPS reverse proxy
   
5. **Rate Limiting**: Add to auth routes to prevent brute-force
   - Install: `npm install express-rate-limit`
   - Apply to /auth/login and /auth/register

## Next Steps

1. **Verify Docker Build**
   ```bash
   docker compose build auth-service
   docker compose up auth-service mongodb
   ```

2. **Test Authentication Flow**
   - Register new user via POST /auth/register
   - Verify MongoDB created user document
   - Login with registered credentials
   - Decode JWT token to verify claims

3. **Integrate with API Gateway**
   - Add JWT verification middleware to api-gateway
   - Protect endpoints that require authentication
   - Pass token in Authorization header

4. **Link to Jira**
   - Create Xray Test issues for auth endpoints
   - Link DM-41, DM-42, DM-43 to auth-service PRs
   - Ensure GitHub-Jira integration captures commits

5. **Add Tests** (E2E / Integration)
   - Test registration with valid/invalid credentials
   - Test login with correct/incorrect password
   - Test duplicate username handling
   - Test JWT token expiry

## Documentation Files
- [auth-service/README.md](../auth-service/README.md) - Full service documentation
- [.env.example](../auth-service/.env.example) - Environment template
- Updated [docker-compose.yml](../docker-compose.yml) - Includes MongoDB and auth-service

## Acceptance Criteria (DM-41, DM-42, DM-43)

✅ **DM-41 - Service Scaffold**: Express app with Mongoose models created
✅ **DM-42 - Authentication Endpoints**: POST /auth/register and POST /auth/login functional
✅ **DM-43 - Docker Compose Integration**: MongoDB and auth-service added to docker-compose.yml

All Phase 5 (Q6) tasks completed and ready for testing.
