📘 Project Conventions — E-commerce Microservices

1. Naming Convention
Services
    Format: {domain}-service
    Examples:
        api-gateway
        auth-service
        product-service

Package
    Format: com.<nguyenhien>.</amazonlike>.<service>
    Example:
        com.nguyenhien.amazonlike.authservice

Code Style
    Class: PascalCase
    Method / Variable: camelCase
    Constant: UPPER_SNAKE_CASE

2. API Convention
Base Path
    /api/v1/{service}/...

Examples
    /api/v1/auth/login
    /api/v1/products
    /api/v1/products/{id}

HTTP Method
    GET → read
    POST → create
    PUT → update
    DELETE → delete

3. Response Format
Success Response
{
  "success": true,
  "data": {},
  "message": "",
  "timestamp": ""
}

4. Error Format
{ "success": false, "message": "", "timestamp": "", "errors": {} }

5. Validation Rules
    Use @Valid for request body
    Validate DTO layer only
    Return structured validation errors

6. Logging Convention
- Log level:
    INFO → normal flow
    WARN → unexpected but recoverable
    ERROR → failure
- Log format:
[Service] METHOD PATH → STATUS

7. Environment Variables
Naming
    Use UPPER_SNAKE_CASE

Examples
    DB_HOST
    DB_PORT
    DB_NAME
    JWT_SECRET
    REDIS_HOST

8. Port Convention
Service	                                    Port
api-gateway	                                8080
auth-service	                            8081
product-service	                            8082

9. Project Structure
project/ 
├── conventions.md 
├── scripts/ 
├── infra/ 
├── services/ │ 
    ├── api-gateway/ │ 
    ├── auth-service/ │ 
    └── product-service/

10. Service Structure
Each service must include:

src/main/java/com/.../
    config/
    common/
    api/
    application/
    domain/
    infrastructure/
    scheduler/

11. Required Components
Each service must have:
    Health endpoint (/actuator/health)
    Swagger UI (/swagger-ui)
    Global Exception Handler
    Config file (application.properties)
    Dockerfile

12. Git Convention
Branch Naming
    feature/{feature-name} 
    fix/{bug-name} 
    chore/{task}

Commit Message
    feat: add auth login 
    fix: resolve routing issue 
    chore: cleanup project structure

13. Development Rules
    Keep services independent
    Avoid premature abstraction
    Duplicate first → refactor later
    Start simple → evolve gradually

14. Phase Rules
    Phase 0–1: No security
    Phase 2+: Introduce authentication
    Phase later: Add observability, tracing, scaling

15. General Principles
    Consistency over perfection
    Readability over cleverness
    Simplicity over complexity

✅ End of Conventions
