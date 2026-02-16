\# TASK: Spring Boot Authentication System



\## Objective

Implement complete JWT-based authentication with email confirmation, password reset, and refresh tokens.



\## Tech Stack

\- Spring Boot 4.0.2

\- Spring Security 6

\- JWT (io.jsonwebtoken 0.12.3)

\- Spring Mail

\- BCrypt password encoding

\- MySQL database



\## Current Project Structure

```

backend/src/main/java/com/tracker/job\_application\_tracker/

├── config/

├── controller/

├── dto/

├── enums/

├── exception/

├── model/

│   ├── Application.java (existing)

│   ├── Document.java (existing)

│   └── StatusHistory.java (existing)

├── repository/

├── service/

│   └── impl/

```



\## Features to Implement



\### 1. New Entities



\*\*User.java\*\*

```java

@Entity

@Table(name = "users")

class User {

&nbsp;   Long id;

&nbsp;   String email (unique, not null);

&nbsp;   String password (hashed with BCrypt);

&nbsp;   String firstName;

&nbsp;   String lastName;

&nbsp;   boolean enabled (default false, true after email confirmation);

&nbsp;   LocalDateTime createdAt;

&nbsp;   LocalDateTime updatedAt;

&nbsp;   

&nbsp;   @OneToMany(mappedBy = "user")

&nbsp;   List<Application> applications;

&nbsp;   

&nbsp;   @Enumerated(EnumType.STRING)

&nbsp;   Role role (USER, ADMIN);

}

```



\*\*ConfirmationToken.java\*\*

```java

@Entity

@Table(name = "confirmation\_tokens")

class ConfirmationToken {

&nbsp;   Long id;

&nbsp;   String token (UUID);

&nbsp;   LocalDateTime createdAt;

&nbsp;   LocalDateTime expiresAt (15 minutes from creation);

&nbsp;   LocalDateTime confirmedAt (nullable);

&nbsp;   

&nbsp;   @ManyToOne

&nbsp;   User user;

}

```



\*\*RefreshToken.java\*\*

```java

@Entity

@Table(name = "refresh\_tokens")

class RefreshToken {

&nbsp;   Long id;

&nbsp;   String token (UUID);

&nbsp;   LocalDateTime expiryDate (7 days from creation);

&nbsp;   

&nbsp;   @ManyToOne

&nbsp;   User user;

}

```



\*\*Role.java (enum)\*\*

```java

enum Role {

&nbsp;   USER,

&nbsp;   ADMIN

}

```



\### 2. Security Configuration



\*\*SecurityConfig.java\*\*

\- Configure HttpSecurity

\- Disable CSRF (stateless JWT)

\- Configure password encoder (BCrypt)

\- Permit public endpoints: /api/auth/\*\*

\- Protect all other endpoints

\- Configure JWT filter



\*\*JwtAuthenticationFilter.java\*\*

\- Extend OncePerRequestFilter

\- Extract JWT from Authorization header

\- Validate token

\- Set authentication in SecurityContext



\*\*JwtService.java\*\*

\- Generate access token (1 hour expiry)

\- Generate refresh token (7 days expiry)

\- Validate token

\- Extract username from token

\- Check if token is expired



\### 3. DTOs



\*\*RegisterRequest\*\*

\- email, password, firstName, lastName



\*\*LoginRequest\*\*

\- email, password



\*\*AuthResponse\*\*

\- accessToken, refreshToken, user (email, firstName, lastName)



\*\*ForgotPasswordRequest\*\*

\- email



\*\*ResetPasswordRequest\*\*

\- token, newPassword



\*\*RefreshTokenRequest\*\*

\- refreshToken



\### 4. Controllers



\*\*AuthController (/api/auth)\*\*



Endpoints:

\- POST /register → Register new user, send confirmation email

\- GET /confirm?token=xxx → Confirm email

\- POST /login → Authenticate, return JWT

\- POST /refresh → Refresh access token

\- POST /forgot-password → Send reset email

\- POST /reset-password → Reset password with token

\- POST /logout → Invalidate refresh token



\### 5. Services



\*\*UserService\*\*

\- createUser(RegisterRequest)

\- findByEmail(email)

\- enableUser(email)

\- updatePassword(email, newPassword)



\*\*EmailService\*\*

\- sendConfirmationEmail(email, token)

\- sendPasswordResetEmail(email, token)



Use Spring Mail with templates.



\*\*ConfirmationTokenService\*\*

\- createToken(user)

\- getToken(token)

\- confirmToken(token)



\*\*RefreshTokenService\*\*

\- createRefreshToken(user)

\- verifyExpiration(token)

\- deleteByUser(user)



\### 6. Modify Existing Entities



\*\*Application.java\*\*

Add:

```java

@ManyToOne

@JoinColumn(name = "user\_id", nullable = false)

private User user;

```



Update ApplicationService to filter by authenticated user.



\### 7. Email Configuration



Use Mailtrap for development:

```properties

spring.mail.host=sandbox.smtp.mailtrap.io

spring.mail.port=2525

spring.mail.username=${MAILTRAP\_USER}

spring.mail.password=${MAILTRAP\_PASSWORD}

```



JWT Secret:

```properties

jwt.secret=${JWT\_SECRET:mySecretKeyForDevelopmentPurposesOnly}

jwt.expiration=3600000

```



\### 8. Exception Handling



Custom exceptions:

\- UserAlreadyExistsException

\- InvalidTokenException

\- TokenExpiredException

\- UserNotEnabledException



Add to GlobalExceptionHandler.



\### 9. Database Migration



Update schema to include new tables.



\## Constraints



\- Follow existing code structure and naming conventions

\- Use constructor injection (no @Autowired on fields)

\- Proper exception handling with meaningful messages

\- Validate all inputs (@Valid, custom validators)

\- BCrypt strength: 12 rounds

\- JWT algorithm: HS256

\- Token expiry: access 1h, refresh 7 days, confirmation 15 min



\## Email Templates



Simple HTML emails:

\- Confirmation: "Click here to confirm: {link}"

\- Password reset: "Click here to reset: {link}"



\## Testing Checklist



After implementation, test with Postman:

\- \[ ] Register user → 201 Created, confirmation email sent

\- \[ ] Confirm email → User enabled

\- \[ ] Login → Returns access + refresh tokens

\- \[ ] Access protected endpoint with token → 200 OK

\- \[ ] Access protected endpoint without token → 401 Unauthorized

\- \[ ] Refresh token → Returns new access token

\- \[ ] Forgot password → Reset email sent

\- \[ ] Reset password → Password updated

\- \[ ] Login with new password → Works



\## Important



\- Do NOT break existing Application/Document/StatusHistory functionality

\- Add User relationship without affecting current features

\- Applications should be filtered by authenticated user automatically

