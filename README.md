# Study Project

- Spring Security
- JWT Token Authentication
- Webflux

# Roles

```mermaid
flowchart TD
    Anonymous[ROLE_ANONYMOUS]
    A[ROLE_A]
    B[ROLE_B]
    User[ROLE_USER]
    Admin[ROLE_ADMIN]
    Anonymous --> Admin
    Anonymous --> User
    User --> A
    User --> B
```

- ROLE_ANONYMOUS: 접근 권한 없음.
- ROLE_USER: /api/enc
- ROLE_A: /api/a/**
- ROLE_B: /api/b/**
- ROLE_ADMIN: /**

# Tables

##### Postgresql Query

- [ddls](./scripts/ddl_pgsql.sql)
- [dmls](./scripts/dml_pgsql.sql)

```mermaid
classDiagram
    class TC_TABLE_NOS {
        -TABLE_NAME : varchar
        -LAST_NO : int
        +getNextId(tblName: String) int
    }
    class TN_USERS {
        -USER_NO : varchar
        -USER_ID : varchar
        -USERNAME : varchar
        -PASSWORD : text
    }
    class TN_MENU {
        -MENU_NO : varchar
        -MENU_NM : varchar
        -MENU_URL : varchar
    }
    class TC_AUTHORITY {
        -AUTH_CODE : varchar
        -AUTH_NAME : varchar
        -UPPER_CODE : varchar
    }
    class TN_MENU_AUTHORITY {
        -MENU_NO : varchar
        -AUTHOR_CODE : varchar
    }
    class TN_USER_AUTHORITY {
        -USER_NO : varchar
        -AUTHOR_CODE : varchar
    }
    TC_TABLE_NOS -- TN_USERS
    TC_TABLE_NOS -- TN_MENU
    TN_USERS -- TN_USER_AUTHORITY
    TN_MENU -- TN_MENU_AUTHORITY
    TN_USER_AUTHORITY -- TC_AUTHORITY
    TN_MENU_AUTHORITY -- TC_AUTHORITY
```

# Error Handling
> Spring Controller에서 Exception이 던져지면 BasicErrorController에서 @RequestMapping("/error) 수신
> 이를 catch하기 위해 컨트롤러를 보조(@ControllerAdvice)하는 ExceptionHandler 빈을 추가하고 interface ErrorController를 구현한
> 새로운 ErrorController를 작성한다.
> ```java
> // Exception Handler일 뿐 /error를 수신하는 컨트롤러는 아래에 있음.
> // @...
> @Component
> @ControllerAdvice // 또는 @RestControllerAdvice
> public class GlobalExceptionHandler {
>     
>     @ExceptionHandler({
>           RuntimeException.class,
>           Exception.class
>     })
>     public void resolveException(Exception ex,
>                                  HttpServletRequest request,
>                                  HttpServletResponse response) { /* ... */ }
> }
> ```
> 
> ```java
> // @RequestMapping("/error") 수신 후 exception 정보를 파싱해서 새 exception을 던져
> // exception handler가 처리할 수 있도록 한다.
> @Controller
> public class CustomErrorController implements ErrorController {
>   @RequestMapping("/error")
>   public void handleError(HttpServletRequest request,
>                           HttpServletResponse response) throws Exception {
>       throw (Exception) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
>   }
> 
> }
> ```