# Authentication System

This Angular frontend includes a complete authentication system with the following features:

## Components

### 1. Login Component (`/login`)
- Username and password form validation
- Real-time form validation with error messages
- Loading states during authentication
- Automatic redirect to dashboard on successful login

### 2. Registration Component (`/register`)
- User registration with password confirmation
- Form validation including password matching
- Success message and automatic redirect to login
- Error handling for registration failures

### 3. Dashboard Component (`/dashboard`)
- Protected route that requires authentication
- Displays welcome message with username
- Logout functionality
- Placeholder for stock management features

## Services

### AuthService
- Handles login and registration HTTP requests
- Manages JWT token storage in localStorage
- Provides authentication state management
- Token validation and expiration checking

## Guards

### AuthGuard
- Protects routes that require authentication
- Automatically redirects unauthenticated users to login
- Checks token validity before allowing access

## Interceptors

### JWT Interceptor
- Automatically adds JWT token to all HTTP requests
- Handles 401 unauthorized responses
- Automatically logs out users when token is invalid

## API Endpoints

The authentication system connects to the following backend endpoints:

- `POST /Auth/register` - User registration
- `POST /Auth/login` - User authentication

## Usage

1. **Registration**: Navigate to `/register` to create a new account
2. **Login**: Navigate to `/login` to authenticate with existing credentials
3. **Dashboard**: Access protected features at `/dashboard` after login
4. **Logout**: Use the logout button in the dashboard

## Route Protection

- `/login` and `/register` are public routes
- `/dashboard` and other protected routes require authentication
- Unauthenticated users are automatically redirected to login

## Token Management

- JWT tokens are stored in localStorage
- Tokens are automatically attached to HTTP requests
- Token expiration is checked and handled automatically
- Invalid tokens trigger automatic logout

## Development

To run the application:

```bash
npm start
```

The application will be available at `http://localhost:4200`

Make sure the backend is running on `http://localhost:8080` for authentication to work properly.
