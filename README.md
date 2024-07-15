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


