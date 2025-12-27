# FoodieDash AI Development Guide

This document provides context and rules for AI when creating code in this project.

---

# Project Overview

FoodieDash is a food ordering backend system built with:

- Java
- Spring Boot
- JPA / Hibernate
- Clean Architecture (Hexagonal)
- Maven

Main features include:

- User management
- Restaurant management
- Menu
- Shopping cart
- Promotions
- Search (Meilisearch)

---

# Architecture

This project follows **Clean Architecture (Hexagonal Architecture)**.

## Dependency Flow

```
interfaces → application → domain
infrastructure → domain
infrastructure → application (sometimes)
```

**Important notes:**
- `interfaces` depends on `application`
- `application` depends on `domain`
- `infrastructure` implements interfaces from `domain` (dependency inversion)
- `domain` does NOT depend on any other layer

## 1. Domain Layer

**Location:**
```
src/main/java/com/ute/foodiedash/domain
```

**Responsibilities:**
- Business entities (domain models)
- Domain services
- Domain rules and business logic
- Repository interfaces (ports)
- Domain events

**Domain MUST NOT depend on:**
- Spring
- JPA
- Infrastructure
- Interfaces

**Structure:**
```
domain/
├── {module}/
│   ├── model/          # Domain entities
│   ├── repository/     # Repository interfaces
│   ├── service/        # Domain services (if needed)
│   ├── enums/          # Domain enums
│   └── event/          # Domain events (if any)
└── common/             # Shared domain code
```

**Examples:**
- `domain/menu/model/Menu`
- `domain/menu/repository/MenuRepository`
- `domain/user/model/User`
- `domain/user/repository/UserRepository`

---

## 2. Application Layer

**Location:**
```
src/main/java/com/ute/foodiedash/application
```

**Responsibilities:**
- Use cases (business workflows)
- Command / Query handlers
- Ports (interfaces for infrastructure)
- Application services

**Structure:**
```
application/
├── {module}/
│   ├── command/        # Write requests (Commands)
│   ├── query/          # Read requests (Query Results)
│   ├── usecase/        # Business logic (Use Cases)
│   └── port/           # Ports (interfaces for infrastructure)
```

**Examples:**
- `application/menu/usecase/CreateMenuUseCase`
- `application/menu/command/CreateMenuCommand`
- `application/menu/query/MenuQueryResult`
- `application/menu/port/ImageUploadPort`

**Rules:**
- Each business action must be a separate **Use Case**
- Use case contains business logic and calls domain repositories
- Use case MUST NOT contain persistence logic directly

---

## 3. Infrastructure Layer

**Location:**
```
src/main/java/com/ute/foodiedash/infrastructure
```

**Responsibilities:**
- Database persistence (JPA)
- External services (Cloudinary, Meilisearch)
- Security
- Configuration
- Event publishing

**Structure:**
```
infrastructure/
├── persistence/
│   └── {module}/
│       ├── adapter/           # Repository implementations
│       ├── jpa/
│       │   ├── entity/        # JPA entities
│       │   ├── mapper/        # JPA mappers (MapStruct)
│       │   └── repository/    # JPA repositories (Spring Data)
├── cloudinary/                # Cloudinary integration
├── search/meilisearch/        # Meilisearch integration
├── security/                  # Security implementations
└── config/                    # Configuration classes
```

**Examples:**
- `infrastructure/persistence/menu/adapter/MenuRepositoryAdapter`
- `infrastructure/persistence/menu/jpa/entity/MenuJpaEntity`
- `infrastructure/persistence/menu/jpa/mapper/MenuJpaMapper`

**Rules:**
- JPA entities only exist in infrastructure
- Repository adapters implement domain repository interfaces
- Use MapStruct for mapping between domain and JPA entities

---

## 4. Interfaces Layer

**Location:**
```
src/main/java/com/ute/foodiedash/interfaces
```

**Responsibilities:**
- REST controllers
- Request / Response DTOs
- API documentation
- Exception handling
- DTO mappers

**Structure:**
```
interfaces/
├── rest/
│   └── {module}/
│       ├── dto/           # DTOs (Request/Response)
│       ├── mapper/        # DTO mappers (MapStruct)
│       └── {Module}Controller.java
├── exception/             # Exception handlers
└── docs/                  # API documentation
```

**Examples:**
- `interfaces/rest/menu/MenuController`
- `interfaces/rest/menu/dto/CreateMenuDTO`
- `interfaces/rest/menu/mapper/MenuDtoMapper`

**Rules:**
- Controller only calls Use Cases, MUST NOT contain business logic
- DTOs only exist in interfaces layer
- Use MapStruct for mapping between DTO and Command/Query

---

# Coding Rules

## Naming Conventions

- **Classes** → PascalCase
- **Variables/Methods** → camelCase
- **Constants** → UPPER_SNAKE_CASE

## DTO Naming

**Request DTOs:**
- `CreateMenuDTO`
- `UpdateMenuDTO`
- `RegisterCustomerDTO`

**Response DTOs:**
- `MenuResponseDTO`
- `MenuDetailDTO`
- `UserResponseDTO`

**Note:** Some modules use `RequestDTO` suffix (e.g., `CreatePromotionRequestDTO`), but the main pattern is `{Action}{Entity}DTO`.

## Mapper Naming

**DTO Mappers:**
- `MenuDtoMapper`
- `UserDtoMapper`
- `PromotionDtoMapper`

**JPA Mappers:**
- `MenuJpaMapper`
- `UserJpaMapper`
- `PromotionJpaMapper`

## Entity Naming

**Domain Models:**
- `Menu`
- `User`
- `Restaurant`

**JPA Entities:**
- `MenuJpaEntity`
- `UserJpaEntity`
- `RestaurantJpaEntity`

**Note:** Domain models and JPA entities MUST NOT be mixed.

---

# Repository Pattern

## Domain Repository Interface

**Location:**
```
domain/{module}/repository/{Entity}Repository
```

**Example:**
```java
package com.ute.foodiedash.domain.menu.repository;

public interface MenuRepository {
    Menu save(Menu menu);
    Optional<Menu> findById(Long id);
    void softDeleteById(Long id);
}
```

## Infrastructure Repository Adapter

**Location:**
```
infrastructure/persistence/{module}/adapter/{Entity}RepositoryAdapter
```

**Example:**
```java
package com.ute.foodiedash.infrastructure.persistence.menu.adapter;

@Component
@RequiredArgsConstructor
public class MenuRepositoryAdapter implements MenuRepository {
    private final MenuJpaRepository jpaRepository;
    private final MenuJpaMapper mapper;
    
    @Override
    public Menu save(Menu menu) {
        MenuJpaEntity jpaEntity = mapper.toJpaEntity(menu);
        MenuJpaEntity saved = jpaRepository.save(jpaEntity);
        return mapper.toDomain(saved);
    }
}
```

**Rules:**
- Adapter implements domain repository interface
- Use JPA mapper to convert between domain and JPA entity
- Adapter is a Spring `@Component`

---

# Use Case Pattern

Each business action must be a separate **Use Case**.

**Location:**
```
application/{module}/usecase/{Action}{Entity}UseCase
```

**Examples:**
- `CreateMenuUseCase`
- `AddItemToCartUseCase`
- `ApplyPromotionUseCase`
- `RegisterCustomerUseCase`

**Use Case Structure:**
```java
@Component
@RequiredArgsConstructor
public class CreateMenuUseCase {
    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;
    
    @Transactional
    public MenuQueryResult execute(CreateMenuCommand command) {
        // 1. Validation
        if (!restaurantRepository.existsById(command.restaurantId())) {
            throw new NotFoundException("Restaurant not found");
        }
        
        // 2. Create domain model
        Menu menu = Menu.create(
            command.restaurantId(),
            command.name(),
            command.startTime(),
            command.endTime()
        );
        
        // 3. Save
        Menu saved = menuRepository.save(menu);
        
        // 4. Return query result
        return MenuQueryResult.from(saved);
    }
}
```

**Rules:**
- Use case must be in application layer
- Use case contains business logic
- Use case calls domain repositories
- Use case returns QueryResult, not domain model directly
- Use `@Transactional` for write operations

---

# Mapper Pattern

## MapStruct Configuration

The project uses **MapStruct** for all mapping operations.

## DTO Mapper

**Location:**
```
interfaces/rest/{module}/mapper/{Entity}DtoMapper
```

**Example:**
```java
@Mapper(componentModel = "spring")
public interface MenuDtoMapper {
    CreateMenuCommand toCommand(CreateMenuDTO dto);
    MenuResponseDTO toResponseDto(MenuQueryResult result);
}
```

**Mapping flow:**
```
DTO → Command (in Controller)
QueryResult → ResponseDTO (in Controller)
```

## JPA Mapper

**Location:**
```
infrastructure/persistence/{module}/jpa/mapper/{Entity}JpaMapper
```

**Example:**
```java
@Mapper(componentModel = "spring")
public interface MenuJpaMapper {
    Menu toDomain(MenuJpaEntity jpaEntity);
    MenuJpaEntity toJpaEntity(Menu domain);
    
    @Mapping(target = "id", ignore = true)
    void updateJpaEntity(Menu domain, @MappingTarget MenuJpaEntity jpaEntity);
}
```

**Mapping flow:**
```
Domain Model ↔ JPA Entity (in Repository Adapter)
```

---

# API Design

## REST Endpoints

**Naming Convention:**
```
GET    /api/v1/{resources}           # List
GET    /api/v1/{resources}/{id}      # Get by ID
POST   /api/v1/{resources}           # Create
PUT    /api/v1/{resources}/{id}      # Update
DELETE /api/v1/{resources}/{id}     # Delete
```

**Examples:**
- `GET /api/v1/restaurants`
- `GET /api/v1/restaurants/{id}`
- `POST /api/v1/restaurants`
- `PUT /api/v1/restaurants/{id}`
- `DELETE /api/v1/restaurants/{id}`

## Controller Structure

```java
@RestController
@RequestMapping("/api/v1/menus")
@RequiredArgsConstructor
public class MenuController {
    private final CreateMenuUseCase createMenuUseCase;
    private final MenuDtoMapper dtoMapper;
    
    @PostMapping
    public ResponseEntity<MenuResponseDTO> createMenu(
            @Valid @RequestBody CreateMenuDTO dto) {
        CreateMenuCommand command = dtoMapper.toCommand(dto);
        MenuQueryResult result = createMenuUseCase.execute(command);
        MenuResponseDTO response = dtoMapper.toResponseDto(result);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
```

**Rules:**
- Controller only calls Use Cases
- Controller uses DTO mapper for conversion
- Controller MUST NOT contain business logic
- Use `@Valid` for request validation

---

# Testing

**Location:**
```
src/test/java
```

**Unit tests should be written for:**
- Use cases
- Domain services
- Domain models (business logic)

**Integration tests should be written for:**
- Repository adapters
- Controllers

---

# Feature Development Process

When implementing a new feature, follow this order:

## 1. Feature Analysis
- Identify required domain models
- Identify business rules
- Identify use cases

## 2. Create Domain Model
- Create domain entity in `domain/{module}/model/`
- Add business logic to domain model
- Create factory methods if needed

## 3. Create Repository Interface
- Define repository interface in `domain/{module}/repository/`
- Only define necessary methods

## 4. Create Use Case
- Create use case in `application/{module}/usecase/`
- Create Command/Query in `application/{module}/command/` or `query/`
- Implement business logic in use case

## 5. Create Infrastructure Adapter
- Create JPA entity in `infrastructure/persistence/{module}/jpa/entity/`
- Create JPA mapper in `infrastructure/persistence/{module}/jpa/mapper/`
- Create JPA repository (Spring Data) in `infrastructure/persistence/{module}/jpa/repository/`
- Create repository adapter in `infrastructure/persistence/{module}/adapter/`

## 6. Create REST Controller
- Create DTO in `interfaces/rest/{module}/dto/`
- Create DTO mapper in `interfaces/rest/{module}/mapper/`
- Create controller in `interfaces/rest/{module}/`

## 7. Testing
- Write unit tests for use case
- Write integration tests for controller

**Note:** Follow this order strictly to ensure Clean Architecture.

---

# Guidelines for AI

When creating code, always follow:

1. ✅ **Always follow Clean Architecture**
   - Domain does not depend on any layer
   - Infrastructure implements domain interfaces
   - Application only depends on domain

2. ✅ **DO NOT put business logic in controllers**
   - Controller only calls Use Cases
   - Business logic must be in Use Cases or Domain Models

3. ✅ **Use Cases interact with domain repositories**
   - Use Case calls domain repository interfaces
   - Infrastructure implements these interfaces

4. ✅ **Infrastructure implements persistence**
   - Use JPA entities
   - Use MapStruct for mapping
   - Repository adapters implement domain repositories

5. ✅ **DTOs only in interfaces layer**
   - DTOs must not appear in domain
   - DTOs must not appear in application (except Command/Query)

6. ✅ **Domain models must be pure**
   - No JPA annotations
   - No Spring dependencies
   - Only contains business logic

7. ✅ **Use MapStruct for all mapping**
   - DTO ↔ Command/Query
   - Domain ↔ JPA Entity

8. ✅ **Each business action is a Use Case**
   - Create a separate use case for each action
   - Use case has a clear name: `{Action}{Entity}UseCase`

---

# Complete Example

## Create Menu Feature

### 1. Domain Model
```java
// domain/menu/model/Menu.java
public class Menu extends BaseEntity {
    public static Menu create(Long restaurantId, String name, 
                              LocalTime startTime, LocalTime endTime) {
        // Validation and business logic
        Menu menu = new Menu();
        menu.restaurantId = restaurantId;
        menu.name = name;
        menu.startTime = startTime;
        menu.endTime = endTime;
        menu.status = MenuStatus.DRAFT;
        return menu;
    }
}
```

### 2. Repository Interface
```java
// domain/menu/repository/MenuRepository.java
public interface MenuRepository {
    Menu save(Menu menu);
    Optional<Menu> findById(Long id);
}
```

### 3. Use Case
```java
// application/menu/usecase/CreateMenuUseCase.java
@Component
@RequiredArgsConstructor
public class CreateMenuUseCase {
    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;
    
    @Transactional
    public MenuQueryResult execute(CreateMenuCommand command) {
        if (!restaurantRepository.existsById(command.restaurantId())) {
            throw new NotFoundException("Restaurant not found");
        }
        
        Menu menu = Menu.create(
            command.restaurantId(),
            command.name(),
            command.startTime(),
            command.endTime()
        );
        
        Menu saved = menuRepository.save(menu);
        return MenuQueryResult.from(saved);
    }
}
```

### 4. Infrastructure
```java
// infrastructure/persistence/menu/jpa/entity/MenuJpaEntity.java
@Entity
@Table(name = "menus")
public class MenuJpaEntity extends BaseJpaEntity {
    // JPA fields
}

// infrastructure/persistence/menu/jpa/mapper/MenuJpaMapper.java
@Mapper(componentModel = "spring")
public interface MenuJpaMapper {
    Menu toDomain(MenuJpaEntity jpaEntity);
    MenuJpaEntity toJpaEntity(Menu domain);
}

// infrastructure/persistence/menu/adapter/MenuRepositoryAdapter.java
@Component
@RequiredArgsConstructor
public class MenuRepositoryAdapter implements MenuRepository {
    private final MenuJpaRepository jpaRepository;
    private final MenuJpaMapper mapper;
    
    @Override
    public Menu save(Menu menu) {
        MenuJpaEntity jpaEntity = mapper.toJpaEntity(menu);
        MenuJpaEntity saved = jpaRepository.save(jpaEntity);
        return mapper.toDomain(saved);
    }
}
```

### 5. Controller
```java
// interfaces/rest/menu/dto/CreateMenuDTO.java
public class CreateMenuDTO {
    @NotNull
    private Long restaurantId;
    
    @NotBlank
    private String name;
    
    private LocalTime startTime;
    private LocalTime endTime;
}

// interfaces/rest/menu/mapper/MenuDtoMapper.java
@Mapper(componentModel = "spring")
public interface MenuDtoMapper {
    CreateMenuCommand toCommand(CreateMenuDTO dto);
    MenuResponseDTO toResponseDto(MenuQueryResult result);
}

// interfaces/rest/menu/MenuController.java
@RestController
@RequestMapping("/api/v1/menus")
@RequiredArgsConstructor
public class MenuController {
    private final CreateMenuUseCase createMenuUseCase;
    private final MenuDtoMapper dtoMapper;
    
    @PostMapping
    public ResponseEntity<MenuResponseDTO> createMenu(
            @Valid @RequestBody CreateMenuDTO dto) {
        CreateMenuCommand command = dtoMapper.toCommand(dto);
        MenuQueryResult result = createMenuUseCase.execute(command);
        MenuResponseDTO response = dtoMapper.toResponseDto(result);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
```

---

# Summary

This project strictly follows **Clean Architecture**. When creating code:

- ✅ Domain is the center, does not depend on any layer
- ✅ Application contains business workflows (Use Cases)
- ✅ Infrastructure implements interfaces from domain
- ✅ Interfaces handle HTTP requests and responses
- ✅ Each layer has clear responsibilities
- ✅ Use MapStruct for all mapping operations
- ✅ Each business action is a separate Use Case

**Remember:** Always follow the dependency flow and do not violate Clean Architecture principles!
