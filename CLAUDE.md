# FoodieDash AI Development Guide

Tài liệu này cung cấp ngữ cảnh và quy tắc cho AI khi tạo code trong dự án này.

---

# Tổng Quan Dự Án

FoodieDash là hệ thống backend đặt món ăn được xây dựng bằng:

- Java
- Spring Boot
- JPA / Hibernate
- Clean Architecture (Hexagonal)
- Maven

Các tính năng chính bao gồm:

- Quản lý người dùng
- Quản lý nhà hàng
- Menu
- Giỏ hàng
- Khuyến mãi
- Tìm kiếm (Meilisearch)

---

# Kiến Trúc

Dự án này tuân theo **Clean Architecture (Hexagonal Architecture)**.

## Dependency Flow

```
interfaces → application → domain
infrastructure → domain
infrastructure → application (sometimes)
```

**Lưu ý quan trọng:**
- `interfaces` phụ thuộc vào `application`
- `application` phụ thuộc vào `domain`
- `infrastructure` implement các interface từ `domain` (dependency inversion)
- `domain` KHÔNG phụ thuộc vào bất kỳ layer nào khác

## 1. Domain Layer

**Vị trí:**
```
src/main/java/com/ute/foodiedash/domain
```

**Trách nhiệm:**
- Business entities (domain models)
- Domain services
- Domain rules và business logic
- Repository interfaces (ports)
- Domain events

**Domain KHÔNG được phụ thuộc vào:**
- Spring
- JPA
- Infrastructure
- Interfaces

**Cấu trúc:**
```
domain/
├── {module}/
│   ├── model/          # Domain entities
│   ├── repository/     # Repository interfaces
│   ├── service/        # Domain services (nếu cần)
│   ├── enums/          # Domain enums
│   └── event/          # Domain events (nếu có)
└── common/             # Shared domain code
```

**Ví dụ:**
- `domain/menu/model/Menu`
- `domain/menu/repository/MenuRepository`
- `domain/user/model/User`
- `domain/user/repository/UserRepository`

---

## 2. Application Layer

**Vị trí:**
```
src/main/java/com/ute/foodiedash/application
```

**Trách nhiệm:**
- Use cases (business workflows)
- Command / Query handlers
- Ports (interfaces cho infrastructure)
- Application services

**Cấu trúc:**
```
application/
├── {module}/
│   ├── command/        # Write requests (Commands)
│   ├── query/          # Read requests (Query Results)
│   ├── usecase/        # Business logic (Use Cases)
│   └── port/           # Ports (interfaces cho infrastructure)
```

**Ví dụ:**
- `application/menu/usecase/CreateMenuUseCase`
- `application/menu/command/CreateMenuCommand`
- `application/menu/query/MenuQueryResult`
- `application/menu/port/ImageUploadPort`

**Quy tắc:**
- Mỗi business action phải là một **Use Case** riêng biệt
- Use case chứa business logic và gọi domain repositories
- Use case KHÔNG được chứa persistence logic trực tiếp

---

## 3. Infrastructure Layer

**Vị trí:**
```
src/main/java/com/ute/foodiedash/infrastructure
```

**Trách nhiệm:**
- Database persistence (JPA)
- External services (Cloudinary, Meilisearch)
- Security
- Configuration
- Event publishing

**Cấu trúc:**
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

**Ví dụ:**
- `infrastructure/persistence/menu/adapter/MenuRepositoryAdapter`
- `infrastructure/persistence/menu/jpa/entity/MenuJpaEntity`
- `infrastructure/persistence/menu/jpa/mapper/MenuJpaMapper`

**Quy tắc:**
- JPA entities chỉ tồn tại trong infrastructure
- Repository adapters implement domain repository interfaces
- Sử dụng MapStruct cho mapping giữa domain và JPA entities

---

## 4. Interfaces Layer

**Vị trí:**
```
src/main/java/com/ute/foodiedash/interfaces
```

**Trách nhiệm:**
- REST controllers
- Request / Response DTOs
- API documentation
- Exception handling
- DTO mappers

**Cấu trúc:**
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

**Ví dụ:**
- `interfaces/rest/menu/MenuController`
- `interfaces/rest/menu/dto/CreateMenuDTO`
- `interfaces/rest/menu/mapper/MenuDtoMapper`

**Quy tắc:**
- Controller chỉ gọi Use Cases, KHÔNG chứa business logic
- DTO chỉ tồn tại trong interfaces layer
- Sử dụng MapStruct cho mapping giữa DTO và Command/Query

---

# Quy Tắc Coding

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

**Lưu ý:** Một số module sử dụng `RequestDTO` suffix (ví dụ: `CreatePromotionRequestDTO`), nhưng pattern chính là `{Action}{Entity}DTO`.

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

**Lưu ý:** KHÔNG được trộn lẫn domain models và JPA entities.

---

# Repository Pattern

## Domain Repository Interface

**Vị trí:**
```
domain/{module}/repository/{Entity}Repository
```

**Ví dụ:**
```java
package com.ute.foodiedash.domain.menu.repository;

public interface MenuRepository {
    Menu save(Menu menu);
    Optional<Menu> findById(Long id);
    void softDeleteById(Long id);
}
```

## Infrastructure Repository Adapter

**Vị trí:**
```
infrastructure/persistence/{module}/adapter/{Entity}RepositoryAdapter
```

**Ví dụ:**
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

**Quy tắc:**
- Adapter implement domain repository interface
- Sử dụng JPA mapper để chuyển đổi giữa domain và JPA entity
- Adapter là Spring `@Component`

---

# Use Case Pattern

Mỗi business action phải là một **Use Case** riêng biệt.

**Vị trí:**
```
application/{module}/usecase/{Action}{Entity}UseCase
```

**Ví dụ:**
- `CreateMenuUseCase`
- `AddItemToCartUseCase`
- `ApplyPromotionUseCase`
- `RegisterCustomerUseCase`

**Cấu trúc Use Case:**
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

**Quy tắc:**
- Use case phải nằm trong application layer
- Use case chứa business logic
- Use case gọi domain repositories
- Use case trả về QueryResult, không trả về domain model trực tiếp
- Sử dụng `@Transactional` cho write operations

---

# Mapper Pattern

## MapStruct Configuration

Dự án sử dụng **MapStruct** cho tất cả mapping operations.

## DTO Mapper

**Vị trí:**
```
interfaces/rest/{module}/mapper/{Entity}DtoMapper
```

**Ví dụ:**
```java
@Mapper(componentModel = "spring")
public interface MenuDtoMapper {
    CreateMenuCommand toCommand(CreateMenuDTO dto);
    MenuResponseDTO toResponseDto(MenuQueryResult result);
}
```

**Mapping flow:**
```
DTO → Command (trong Controller)
QueryResult → ResponseDTO (trong Controller)
```

## JPA Mapper

**Vị trí:**
```
infrastructure/persistence/{module}/jpa/mapper/{Entity}JpaMapper
```

**Ví dụ:**
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
Domain Model ↔ JPA Entity (trong Repository Adapter)
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

**Ví dụ:**
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

**Quy tắc:**
- Controller chỉ gọi Use Cases
- Controller sử dụng DTO mapper để chuyển đổi
- Controller KHÔNG chứa business logic
- Sử dụng `@Valid` cho request validation

---

# Testing

**Vị trí:**
```
src/test/java
```

**Unit tests nên được viết cho:**
- Use cases
- Domain services
- Domain models (business logic)

**Integration tests nên được viết cho:**
- Repository adapters
- Controllers

---

# Quy Trình Phát Triển Tính Năng

Khi implement một tính năng mới, tuân theo thứ tự sau:

## 1. Phân tích tính năng
- Xác định domain model cần thiết
- Xác định business rules
- Xác định use cases

## 2. Tạo Domain Model
- Tạo domain entity trong `domain/{module}/model/`
- Thêm business logic vào domain model
- Tạo factory methods nếu cần

## 3. Tạo Repository Interface
- Định nghĩa repository interface trong `domain/{module}/repository/`
- Chỉ định nghĩa methods cần thiết

## 4. Tạo Use Case
- Tạo use case trong `application/{module}/usecase/`
- Tạo Command/Query trong `application/{module}/command/` hoặc `query/`
- Implement business logic trong use case

## 5. Tạo Infrastructure Adapter
- Tạo JPA entity trong `infrastructure/persistence/{module}/jpa/entity/`
- Tạo JPA mapper trong `infrastructure/persistence/{module}/jpa/mapper/`
- Tạo JPA repository (Spring Data) trong `infrastructure/persistence/{module}/jpa/repository/`
- Tạo repository adapter trong `infrastructure/persistence/{module}/adapter/`

## 6. Tạo REST Controller
- Tạo DTO trong `interfaces/rest/{module}/dto/`
- Tạo DTO mapper trong `interfaces/rest/{module}/mapper/`
- Tạo controller trong `interfaces/rest/{module}/`

## 7. Testing
- Viết unit tests cho use case
- Viết integration tests cho controller

**Lưu ý:** Tuân theo thứ tự này một cách nghiêm ngặt để đảm bảo Clean Architecture.

---

# Hướng Dẫn Cho AI

Khi tạo code, luôn tuân theo:

1. ✅ **Luôn tuân theo Clean Architecture**
   - Domain không phụ thuộc vào bất kỳ layer nào
   - Infrastructure implement domain interfaces
   - Application chỉ phụ thuộc vào domain

2. ✅ **KHÔNG đặt business logic trong controllers**
   - Controller chỉ gọi Use Cases
   - Business logic phải ở Use Cases hoặc Domain Models

3. ✅ **Use Cases tương tác với domain repositories**
   - Use Case gọi domain repository interfaces
   - Infrastructure implement các interfaces này

4. ✅ **Infrastructure implement persistence**
   - Sử dụng JPA entities
   - Sử dụng MapStruct cho mapping
   - Repository adapters implement domain repositories

5. ✅ **DTO chỉ ở interfaces layer**
   - DTO không được xuất hiện trong domain
   - DTO không được xuất hiện trong application (trừ Command/Query)

6. ✅ **Domain models phải thuần khiết**
   - Không có JPA annotations
   - Không có Spring dependencies
   - Chỉ chứa business logic

7. ✅ **Sử dụng MapStruct cho tất cả mapping**
   - DTO ↔ Command/Query
   - Domain ↔ JPA Entity

8. ✅ **Mỗi business action là một Use Case**
   - Tạo use case riêng cho mỗi action
   - Use case có tên rõ ràng: `{Action}{Entity}UseCase`

---

# Ví Dụ Hoàn Chỉnh

## Tạo Menu Feature

### 1. Domain Model
```java
// domain/menu/model/Menu.java
public class Menu extends BaseEntity {
    public static Menu create(Long restaurantId, String name, 
                              LocalTime startTime, LocalTime endTime) {
        // Validation và business logic
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

# Tổng Kết

Dự án này tuân theo **Clean Architecture** một cách nghiêm ngặt. Khi tạo code:

- ✅ Domain là trung tâm, không phụ thuộc vào bất kỳ layer nào
- ✅ Application chứa business workflows (Use Cases)
- ✅ Infrastructure implement các interfaces từ domain
- ✅ Interfaces xử lý HTTP requests và responses
- ✅ Mỗi layer có trách nhiệm rõ ràng
- ✅ Sử dụng MapStruct cho tất cả mapping operations
- ✅ Mỗi business action là một Use Case riêng biệt

**Nhớ:** Luôn tuân theo dependency flow và không vi phạm các nguyên tắc Clean Architecture!
