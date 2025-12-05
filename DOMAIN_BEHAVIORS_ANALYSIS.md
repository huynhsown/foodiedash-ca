# 📋 Phân Tích Domain Behaviors Còn Thiếu — FoodieDash

> **Ngày phân tích**: 05/03/2026  
> **Dự án**: FoodieDash — Clean Architecture (Spring Boot 4.0.3, Java 21)  
> **Mục tiêu**: Liệt kê toàn bộ behavior còn thiếu trong các domain entity và hướng dẫn áp dụng vào use cases

---

## Mục Lục

1. [Tổng Quan Vấn Đề](#1-tổng-quan-vấn-đề)
2. [Restaurant](#2-restaurant--thiếu-4-behaviors)
3. [Menu](#3-menu--thiếu-5-behaviors)
4. [MenuItem](#4-menuitem--thiếu-4-behaviors)
5. [MenuItemOption](#5-menuitemoption--thiếu-2-behaviors)
6. [MenuItemOptionValue](#6-menuitemoptionvalue--không-cần-thêm)
7. [RestaurantBusinessHour](#7-restaurantbusinesshour--thiếu-2-behaviors)
8. [RestaurantPause](#8-restaurantpause--thiếu-2-behaviors)
9. [RestaurantPreparationSetting](#9-restaurantpreparationsetting--thiếu-2-behaviors)
10. [RestaurantRating](#10-restaurantrating--thiếu-1-behavior)
11. [RestaurantCategory](#11-restaurantcategory--không-cần-thêm)
12. [RestaurantCategoryMap](#12-restaurantcategorymap--không-cần-thêm)
13. [RestaurantImage](#13-restaurantimage--không-cần-thêm)
14. [Cart](#14-cart--thiếu-1-behavior)
15. [CartItem](#15-cartitem--thiếu-5-behaviors)
16. [CartItemOption](#16-cartitemoption--không-cần-thêm)
17. [CartItemOptionValue](#17-cartitemoptionvalue--không-cần-thêm)
18. [Bảng Tổng Hợp](#18-bảng-tổng-hợp)
19. [Ưu Tiên Thực Hiện](#19-ưu-tiên-thực-hiện)

---

## 1. Tổng Quan Vấn Đề

### Anemic Domain Model là gì?

Trong dự án hiện tại, hầu hết domain models chỉ là **data holder** (chỉ có getter/setter) mà không chứa business logic. Các business rules nằm hoàn toàn trong **Use Cases** (Application Layer). Điều này vi phạm nguyên tắc **Rich Domain Model** của Clean Architecture / DDD.

### Hậu quả

- **Code duplication**: Cùng một business rule bị viết lại ở nhiều use cases
- **Khó maintain**: Khi business rule thay đổi, phải tìm sửa ở nhiều nơi
- **Khó test**: Không thể unit test business logic độc lập khỏi use case
- **Vi phạm Single Responsibility**: Use case vừa orchestrate vừa chứa business rules

### Nguyên tắc sửa

1. **Business rules** (validate, state transition, calculation) → đưa vào **Domain Model**
2. **Orchestration** (gọi repo, gọi port, compose kết quả) → giữ ở **Use Case**
3. **Factory methods** → thay thế constructor + setter chain, đảm bảo invariants

---

## 2. Restaurant — Thiếu 4 Behaviors

### Hiện trạng

```java
// File: domain/restaurant/model/Restaurant.java
@Getter
@Setter
public class Restaurant extends BaseEntity {
    private Long id;
    private String code;
    private String slug;
    private String name;
    private String description;
    private String address;
    private String phone;
    private BigDecimal lat;
    private BigDecimal lng;
    private String status; // ⚠️ Dùng String thay vì Enum

    public void updateSlug(String slug) { this.slug = slug; }
    public boolean isActive() { return "ACTIVE".equals(status); }
}
```

### Vấn đề phát hiện

| Vấn đề | Vị trí hiện tại | Ảnh hưởng |
|---|---|---|
| `status` dùng `String` | `Restaurant.java` | Magic string "ACTIVE", dễ sai chính tả |
| Constructor logic trong UseCase | `CreateRestaurantUseCase` dòng 24-32 | 8 dòng setter thủ công |
| Guard `ensureActive` duplicate | `GetRestaurantDetailBySlugUseCase` dòng 41-43, `GetRestaurantSnapshotByIdUseCase` dòng 28-30 | Duplicate logic |
| Slug validation | `CreateRestaurantUseCase` dòng 35-41 | Logic nằm ngoài domain |

### Cần tạo Enum mới

```java
// File MỚI: domain/restaurant/model/RestaurantStatus.java
package com.ute.foodiedash.domain.restaurant.model;

public enum RestaurantStatus {
    ACTIVE,
    INACTIVE,
    SUSPENDED
}
```

### Code mới cho Restaurant

```java
// File: domain/restaurant/model/Restaurant.java
package com.ute.foodiedash.domain.restaurant.model;

import com.ute.foodiedash.domain.common.model.BaseEntity;
import com.ute.foodiedash.domain.common.exception.BadRequestException;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class Restaurant extends BaseEntity {
    private Long id;
    private String code;
    private String slug;
    private String name;
    private String description;
    private String address;
    private String phone;
    private BigDecimal lat;
    private BigDecimal lng;
    private RestaurantStatus status; // ← ĐỔI SANG ENUM

    // ========== BEHAVIOR MỚI 1: Factory method ==========
    public static Restaurant create(String code, String name, String description,
                                     String address, String phone,
                                     BigDecimal lat, BigDecimal lng, String slug) {
        Restaurant restaurant = new Restaurant();
        restaurant.code = code;
        restaurant.name = name;
        restaurant.description = description;
        restaurant.address = address;
        restaurant.phone = phone;
        restaurant.lat = lat;
        restaurant.lng = lng;
        restaurant.slug = slug;
        restaurant.status = RestaurantStatus.ACTIVE;
        return restaurant;
    }

    // ========== BEHAVIOR MỚI 2: Guard method ==========
    public void ensureActive() {
        if (!isActive()) {
            throw new BadRequestException("Restaurant is not active");
        }
    }

    // ========== BEHAVIOR MỚI 3: Assign slug với validation ==========
    public void assignSlug(String slug) {
        if (slug == null || slug.isBlank()) {
            throw new IllegalArgumentException("Slug must not be blank");
        }
        this.slug = slug;
    }

    // ========== BEHAVIOR MỚI 4: Check coordinates ==========
    public boolean hasCoordinates() {
        return lat != null && lng != null;
    }

    public boolean isActive() {
        return status == RestaurantStatus.ACTIVE;
    }
}
```

### Áp dụng vào Use Cases

#### CreateRestaurantUseCase

```java
// ❌ TRƯỚC (8 dòng setter thủ công)
Restaurant restaurant = new Restaurant();
restaurant.setCode(command.code());
restaurant.setName(command.name());
restaurant.setDescription(command.description());
restaurant.setAddress(command.address());
restaurant.setPhone(command.phone());
restaurant.setLat(command.lat());
restaurant.setLng(command.lng());
restaurant.setStatus(command.status());
// ... slug logic ...
restaurant.setSlug(slug);

// ✅ SAU (1 dòng factory method)
Restaurant restaurant = Restaurant.create(
    command.code(), command.name(), command.description(),
    command.address(), command.phone(),
    command.lat(), command.lng(), slug
);
```

#### GetRestaurantDetailBySlugUseCase (dòng 41-43)

```java
// ❌ TRƯỚC
if (!restaurant.isActive()) {
    throw new BadRequestException("Restaurant doesn't active");
}

// ✅ SAU
restaurant.ensureActive();
```

#### GetRestaurantSnapshotByIdUseCase (dòng 28-30)

```java
// ❌ TRƯỚC (duplicate)
if (!restaurant.isActive()) {
    throw new BadRequestException("Restaurant doesn't active");
}

// ✅ SAU
restaurant.ensureActive();
```

#### GetRestaurantDetailBySlugUseCase (dòng 75)

```java
// ❌ TRƯỚC
if (lat != null && lng != null && restaurant.getLat() != null && restaurant.getLng() != null) {

// ✅ SAU
if (lat != null && lng != null && restaurant.hasCoordinates()) {
```

---

## 3. Menu — Thiếu 5 Behaviors

### Hiện trạng

```java
// File: domain/menu/model/Menu.java
@Getter
@Setter
public class Menu extends BaseEntity {
    private Long id;
    private Long restaurantId;
    private String name;
    private LocalTime startTime;
    private LocalTime endTime;
    private MenuStatus status;
    // ⚠️ KHÔNG CÓ BEHAVIOR NÀO
}
```

### Vấn đề phát hiện

| Vấn đề | Vị trí hiện tại | Ảnh hưởng |
|---|---|---|
| Không có factory method | `CreateMenuUseCase` dòng 26-31 | Status mặc định DRAFT đặt ở use case |
| State transition `submit()` ở use case | `SubmitMenuUseCase` dòng 25-28, 36 | Business rule nằm sai layer |
| Không có `ensureActive()` | `AddToCartUseCase` dòng 92-97 | Logic check active nằm ở use case |

### Code mới cho Menu

```java
// File: domain/menu/model/Menu.java
package com.ute.foodiedash.domain.menu.model;

import com.ute.foodiedash.domain.common.model.BaseEntity;
import com.ute.foodiedash.domain.common.exception.BadRequestException;
import com.ute.foodiedash.domain.menu.enums.MenuStatus;
import lombok.Getter;

import java.time.LocalTime;

@Getter
public class Menu extends BaseEntity {
    private Long id;
    private Long restaurantId;
    private String name;
    private LocalTime startTime;
    private LocalTime endTime;
    private MenuStatus status;

    // ========== BEHAVIOR MỚI 1: Factory method ==========
    public static Menu create(Long restaurantId, String name,
                               LocalTime startTime, LocalTime endTime) {
        Menu menu = new Menu();
        menu.restaurantId = restaurantId;
        menu.name = name;
        menu.startTime = startTime;
        menu.endTime = endTime;
        menu.status = MenuStatus.DRAFT; // invariant: menu luôn bắt đầu ở DRAFT
        return menu;
    }

    // ========== BEHAVIOR MỚI 2: State transition — Submit ==========
    public void submit() {
        if (this.status != MenuStatus.DRAFT) {
            throw new BadRequestException(
                "Menu is not in DRAFT status. Current status: " + this.status);
        }
        this.status = MenuStatus.ACTIVE;
    }

    // ========== BEHAVIOR MỚI 3: Query methods ==========
    public boolean isDraft() {
        return status == MenuStatus.DRAFT;
    }

    public boolean isActive() {
        return status == MenuStatus.ACTIVE;
    }

    // ========== BEHAVIOR MỚI 4: Guard method ==========
    public void ensureActive() {
        if (!isActive()) {
            throw new BadRequestException("Menu is not active");
        }
    }

    // ========== BEHAVIOR MỚI 5: Time range validation ==========
    public void validate() {
        if (startTime != null && endTime != null && startTime.isAfter(endTime)) {
            throw new IllegalArgumentException("Start time must be before end time");
        }
    }
}
```

### Áp dụng vào Use Cases

#### CreateMenuUseCase

```java
// ❌ TRƯỚC
Menu menu = new Menu();
menu.setRestaurantId(command.restaurantId());
menu.setName(command.name());
menu.setStartTime(command.startTime());
menu.setEndTime(command.endTime());
menu.setStatus(MenuStatus.DRAFT);

// ✅ SAU
Menu menu = Menu.create(
    command.restaurantId(), command.name(),
    command.startTime(), command.endTime()
);
```

#### SubmitMenuUseCase

```java
// ❌ TRƯỚC
if (menu.getStatus() != MenuStatus.DRAFT) {
    throw new BadRequestException(
        "Menu with id " + id + " is not in DRAFT status. Current status: " + menu.getStatus());
}
// ... check hasMenuItems ...
menu.setStatus(MenuStatus.ACTIVE);

// ✅ SAU
// ... check hasMenuItems ...
menu.submit(); // Domain model tự validate trạng thái + chuyển sang ACTIVE
```

#### AddToCartUseCase.validateAddToCart() (dòng 92-97)

```java
// ❌ TRƯỚC
Menu menu = menuRepository.findById(menuItem.menuId())
    .orElseThrow(() -> new NotFoundException("Menu not found"));
if (menu.getStatus() != MenuStatus.ACTIVE) {
    throw new BadRequestException("Menu is not active");
}

// ✅ SAU
Menu menu = menuRepository.findById(menuItem.menuId())
    .orElseThrow(() -> new NotFoundException("Menu not found"));
menu.ensureActive();
```

---

## 4. MenuItem — Thiếu 4 Behaviors

### Hiện trạng

```java
// File: domain/menu/model/MenuItem.java
@Getter
@Setter
public class MenuItem extends BaseEntity {
    private Long id;
    private Long menuId;
    private Long restaurantId;
    private String name;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private MenuItemStatus status;
    // ⚠️ KHÔNG CÓ BEHAVIOR NÀO
}
```

### Vấn đề phát hiện

| Vấn đề | Vị trí hiện tại | Ảnh hưởng |
|---|---|---|
| Không có factory method | `CreateMenuItemUseCase` dòng 32-38 | Status mặc định ACTIVE đặt ở use case |
| Check active nằm ở use case | `AddToCartUseCase` dòng 88-90 | Business rule sai layer |
| Check thuộc restaurant | `AddToCartUseCase` dòng 84-86 | Business rule sai layer |

### Code mới cho MenuItem

```java
// File: domain/menu/model/MenuItem.java
package com.ute.foodiedash.domain.menu.model;

import com.ute.foodiedash.domain.common.model.BaseEntity;
import com.ute.foodiedash.domain.common.exception.BadRequestException;
import com.ute.foodiedash.domain.menu.enums.MenuItemStatus;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class MenuItem extends BaseEntity {
    private Long id;
    private Long menuId;
    private Long restaurantId;
    private String name;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private MenuItemStatus status;

    // ========== BEHAVIOR MỚI 1: Factory method ==========
    public static MenuItem create(Long menuId, Long restaurantId, String name,
                                   String description, BigDecimal price) {
        MenuItem item = new MenuItem();
        item.menuId = menuId;
        item.restaurantId = restaurantId;
        item.name = name;
        item.description = description;
        item.price = price;
        item.status = MenuItemStatus.ACTIVE; // invariant: mặc định ACTIVE
        return item;
    }

    // ========== BEHAVIOR MỚI 2: Guard — ensure active ==========
    public void ensureActive() {
        if (status != MenuItemStatus.ACTIVE) {
            throw new BadRequestException("Menu item is not active");
        }
    }

    public boolean isActive() {
        return status == MenuItemStatus.ACTIVE;
    }

    // ========== BEHAVIOR MỚI 3: Ensure belongs to restaurant ==========
    public void ensureBelongsToRestaurant(Long restaurantId) {
        if (!this.restaurantId.equals(restaurantId)) {
            throw new BadRequestException("Menu item does not belong to restaurant");
        }
    }

    // ========== BEHAVIOR MỚI 4: Assign image ==========
    public void assignImage(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
```

### Áp dụng vào Use Cases

#### CreateMenuItemUseCase

```java
// ❌ TRƯỚC
MenuItem menuItem = new MenuItem();
menuItem.setMenuId(command.menuId());
menuItem.setRestaurantId(menu.getRestaurantId());
menuItem.setName(command.name());
menuItem.setDescription(command.description());
menuItem.setPrice(command.price());
menuItem.setStatus(MenuItemStatus.ACTIVE);

// ✅ SAU
MenuItem menuItem = MenuItem.create(
    command.menuId(), menu.getRestaurantId(),
    command.name(), command.description(), command.price()
);
```

#### Xử lý image trong CreateMenuItemUseCase

```java
// ❌ TRƯỚC
menuItem.setImageUrl(imageUrl);

// ✅ SAU
menuItem.assignImage(imageUrl);
```

#### AddToCartUseCase.validateAddToCart() (dòng 84-90)

> **Lưu ý**: Hiện tại use case validate dựa trên `MenuItemQueryResult` (record). Để áp dụng behavior này cần refactor lại flow: load domain model `MenuItem` thay vì query result, hoặc thêm method tương đương trên query result.

```java
// ❌ TRƯỚC (dùng query result)
if (!command.restaurantId().equals(menuItem.restaurantId())) {
    throw new BadRequestException("Menu item does not belong to restaurant");
}
if (menuItem.status() != MenuItemStatus.ACTIVE) {
    throw new BadRequestException("Menu item is not active");
}

// ✅ SAU (nếu refactor sang domain model)
menuItemDomain.ensureBelongsToRestaurant(command.restaurantId());
menuItemDomain.ensureActive();
```

---

## 5. MenuItemOption — Thiếu 2 Behaviors

### Hiện trạng

```java
// File: domain/menu/model/MenuItemOption.java
@Getter
@Setter
public class MenuItemOption extends BaseEntity {
    private Long id;
    private Long menuItemId;
    private String name;
    private Boolean required = false;
    private Integer minValue;
    private Integer maxValue;
    // ⚠️ KHÔNG CÓ BEHAVIOR NÀO
}
```

### Vấn đề phát hiện — CODE DUPLICATE NGHIÊM TRỌNG

Logic validate selection count bị **duplicate hoàn toàn** ở 2 use cases:

- `AddToCartUseCase.validateOptions()` — dòng 141-150
- `UpdateCartItemUseCase.validateOptions()` — dòng 107-116

Đây là ~15 dòng code giống hệt nhau!

### Code mới cho MenuItemOption

```java
// File: domain/menu/model/MenuItemOption.java
package com.ute.foodiedash.domain.menu.model;

import com.ute.foodiedash.domain.common.model.BaseEntity;
import com.ute.foodiedash.domain.common.exception.BadRequestException;
import lombok.Getter;

@Getter
public class MenuItemOption extends BaseEntity {
    private Long id;
    private Long menuItemId;
    private String name;
    private Boolean required = false;
    private Integer minValue;
    private Integer maxValue;

    // ========== BEHAVIOR MỚI 1: Validate selection count ==========
    /**
     * Validate số lượng option values đã chọn dựa trên rules:
     * - Nếu required = true → phải chọn ít nhất 1
     * - Nếu có minValue → số lượng chọn >= minValue
     * - Nếu có maxValue → số lượng chọn <= maxValue
     */
    public void validateSelectionCount(int selectedCount) {
        if (Boolean.TRUE.equals(required) && selectedCount == 0) {
            throw new BadRequestException("Option is required: " + name);
        }
        if (minValue != null && selectedCount < minValue) {
            throw new BadRequestException("Option min value violated: " + name);
        }
        if (maxValue != null && selectedCount > maxValue) {
            throw new BadRequestException("Option max value violated: " + name);
        }
    }

    // ========== BEHAVIOR MỚI 2: Check required ==========
    public boolean isRequired() {
        return Boolean.TRUE.equals(required);
    }
}
```

### Áp dụng vào Use Cases — XÓA BỎ DUPLICATE

#### AddToCartUseCase.validateOptions() (dòng 141-150)

```java
// ❌ TRƯỚC (~10 dòng)
int selectedCount = optionCommand.values().size();
if (Boolean.TRUE.equals(option.required()) && selectedCount == 0) {
    throw new BadRequestException("Option is required: " + option.name());
}
if (option.minValue() != null && selectedCount < option.minValue()) {
    throw new BadRequestException("Option min value violated: " + option.name());
}
if (option.maxValue() != null && selectedCount > option.maxValue()) {
    throw new BadRequestException("Option max value violated: " + option.name());
}

// ✅ SAU (1 dòng) — cần refactor để validate trên domain model
menuItemOption.validateSelectionCount(optionCommand.values().size());
```

#### UpdateCartItemUseCase.validateOptions() (dòng 107-116)

```java
// ❌ TRƯỚC (duplicate y hệt)
int selectedCount = optionCommand.values().size();
if (Boolean.TRUE.equals(option.required()) && selectedCount == 0) { ... }
if (option.minValue() != null && selectedCount < option.minValue()) { ... }
if (option.maxValue() != null && selectedCount > option.maxValue()) { ... }

// ✅ SAU (1 dòng)
menuItemOption.validateSelectionCount(optionCommand.values().size());
```

#### Check required missing (cả 2 use case)

```java
// ❌ TRƯỚC
if (Boolean.TRUE.equals(option.required()) && !requestOptionIds.contains(option.id())) {
    throw new BadRequestException("Missing required option: " + option.name());
}

// ✅ SAU
if (menuItemOption.isRequired() && !requestOptionIds.contains(option.id())) {
    throw new BadRequestException("Missing required option: " + option.name());
}
```

> **Lưu ý quan trọng**: Hiện tại cả 2 use case validate dựa trên `MenuItemOptionQueryResult` (record). Để áp dụng behavior trên domain model, có 2 cách:
> 1. **Cách 1**: Refactor use case để load domain model `MenuItemOption` thay vì query result
> 2. **Cách 2**: Giữ nguyên flow nhưng tạo static validation method trên domain model rồi gọi từ use case

---

## 6. MenuItemOptionValue — Không cần thêm

Entity đơn giản, chỉ chứa data. Không có business rule đặc biệt.

```java
// Giữ nguyên
public class MenuItemOptionValue extends BaseEntity {
    private Long id;
    private Long optionId;
    private String name;
    private BigDecimal extraPrice;
}
```

---

## 7. RestaurantBusinessHour — Thiếu 2 Behaviors

### Hiện trạng

```java
// File: domain/restaurant/model/RestaurantBusinessHour.java
@Getter
@Setter
public class RestaurantBusinessHour extends BaseEntity {
    private Long id;
    private Long restaurantId;
    private Integer dayOfWeek;
    private LocalTime openTime;
    private LocalTime closeTime;
    // ⚠️ KHÔNG CÓ BEHAVIOR NÀO
}
```

### Vấn đề phát hiện

Logic check restaurant open nằm ở `infrastructure/util/RestaurantUtils.java` (dòng 22-46). Đây là **business logic thuần túy** nhưng đang nằm ở **infrastructure layer** — vi phạm Dependency Rule!

### Code mới cho RestaurantBusinessHour

```java
// File: domain/restaurant/model/RestaurantBusinessHour.java
package com.ute.foodiedash.domain.restaurant.model;

import com.ute.foodiedash.domain.common.model.BaseEntity;
import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Getter
public class RestaurantBusinessHour extends BaseEntity {
    private Long id;
    private Long restaurantId;
    private Integer dayOfWeek;
    private LocalTime openTime;
    private LocalTime closeTime;

    // ========== BEHAVIOR MỚI 1: Check if open at specific day + time ==========
    /**
     * Kiểm tra xem business hour này có bao gồm ngày + giờ đã cho hay không.
     *
     * @param day  ngày trong tuần (DayOfWeek)
     * @param time giờ hiện tại
     * @return true nếu restaurant mở vào thời điểm đó
     */
    public boolean isOpenAt(DayOfWeek day, LocalTime time) {
        return this.dayOfWeek == day.getValue()
            && !time.isBefore(openTime)
            && !time.isAfter(closeTime);
    }

    // ========== BEHAVIOR MỚI 2: Validation ==========
    public void validate() {
        if (dayOfWeek < 1 || dayOfWeek > 7) {
            throw new IllegalArgumentException("Day of week must be between 1 and 7");
        }
        if (openTime != null && closeTime != null && openTime.isAfter(closeTime)) {
            throw new IllegalArgumentException("Open time must be before close time");
        }
    }
}
```

### Áp dụng vào RestaurantUtils (refactor)

```java
// ❌ TRƯỚC (infrastructure/util/RestaurantUtils.java dòng 34-41)
return businessHours.stream()
    .anyMatch(bh -> {
        if (bh.getDayOfWeek() == currentDay.getValue()) {
            return !currentTime.isBefore(bh.getOpenTime())
                && !currentTime.isAfter(bh.getCloseTime());
        }
        return false;
    });

// ✅ SAU
DayOfWeek currentDay = DayOfWeek.from(LocalDate.now());
LocalTime currentTime = LocalTime.now();
return businessHours.stream()
    .anyMatch(bh -> bh.isOpenAt(currentDay, currentTime));
```

### Áp dụng vào CreateRestaurantBusinessHourUseCase

```java
// ❌ TRƯỚC — không validate gì
RestaurantBusinessHour businessHour = new RestaurantBusinessHour();
businessHour.setRestaurantId(command.restaurantId());
businessHour.setDayOfWeek(command.dayOfWeek());
businessHour.setOpenTime(command.openTime());
businessHour.setCloseTime(command.closeTime());

// ✅ SAU — validate trước khi save
RestaurantBusinessHour businessHour = new RestaurantBusinessHour();
businessHour.setRestaurantId(command.restaurantId());
businessHour.setDayOfWeek(command.dayOfWeek());
businessHour.setOpenTime(command.openTime());
businessHour.setCloseTime(command.closeTime());
businessHour.validate(); // ← Validate domain rules
```

---

## 8. RestaurantPause — Thiếu 2 Behaviors

### Hiện trạng

```java
// File: domain/restaurant/model/RestaurantPause.java
@Getter
@Setter
public class RestaurantPause extends BaseEntity {
    private Long id;
    private Long restaurantId;
    private String reason;
    private Instant pausedFrom;
    private Instant pausedTo;
    // ⚠️ KHÔNG CÓ BEHAVIOR NÀO
}
```

### Code mới cho RestaurantPause

```java
// File: domain/restaurant/model/RestaurantPause.java
package com.ute.foodiedash.domain.restaurant.model;

import com.ute.foodiedash.domain.common.model.BaseEntity;
import lombok.Getter;

import java.time.Instant;

@Getter
public class RestaurantPause extends BaseEntity {
    private Long id;
    private Long restaurantId;
    private String reason;
    private Instant pausedFrom;
    private Instant pausedTo;

    // ========== BEHAVIOR MỚI 1: Check currently paused ==========
    /**
     * Kiểm tra xem restaurant có đang trong trạng thái tạm dừng tại thời điểm hiện tại.
     */
    public boolean isCurrentlyPaused() {
        Instant now = Instant.now();
        return pausedFrom != null && pausedTo != null
            && !now.isBefore(pausedFrom)
            && !now.isAfter(pausedTo);
    }

    // ========== BEHAVIOR MỚI 2: Validation ==========
    public void validate() {
        if (pausedFrom != null && pausedTo != null && pausedFrom.isAfter(pausedTo)) {
            throw new IllegalArgumentException("pausedFrom must be before pausedTo");
        }
        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException("Reason must not be blank");
        }
    }
}
```

### Áp dụng vào CreateRestaurantPauseUseCase

```java
// ❌ TRƯỚC — không validate
RestaurantPause pause = new RestaurantPause();
pause.setRestaurantId(command.restaurantId());
pause.setReason(command.reason());
pause.setPausedFrom(command.pausedFrom());
pause.setPausedTo(command.pausedTo());
RestaurantPause saved = restaurantPauseRepository.save(pause);

// ✅ SAU — validate trước khi save
RestaurantPause pause = new RestaurantPause();
pause.setRestaurantId(command.restaurantId());
pause.setReason(command.reason());
pause.setPausedFrom(command.pausedFrom());
pause.setPausedTo(command.pausedTo());
pause.validate(); // ← Validate domain rules
RestaurantPause saved = restaurantPauseRepository.save(pause);
```

---

## 9. RestaurantPreparationSetting — Thiếu 2 Behaviors

### Hiện trạng

```java
// File: domain/restaurant/model/RestaurantPreparationSetting.java
@Getter
@Setter
public class RestaurantPreparationSetting extends BaseEntity {
    private Long id;
    private Long restaurantId;
    private Integer prepTimeMin;
    private Integer prepTimeMax;
    private Integer slotDuration;
    private Integer maxOrdersPerSlot;
    // ⚠️ KHÔNG CÓ BEHAVIOR NÀO
}
```

### Vấn đề phát hiện — CODE DUPLICATE

Logic `(prepTimeMin + prepTimeMax) / 2` bị duplicate ở 2 use cases:

- `GetRestaurantDetailBySlugUseCase` dòng 84
- `GetRestaurantSnapshotByIdUseCase` dòng 50

### Code mới cho RestaurantPreparationSetting

```java
// File: domain/restaurant/model/RestaurantPreparationSetting.java
package com.ute.foodiedash.domain.restaurant.model;

import com.ute.foodiedash.domain.common.model.BaseEntity;
import lombok.Getter;

@Getter
public class RestaurantPreparationSetting extends BaseEntity {
    private Long id;
    private Long restaurantId;
    private Integer prepTimeMin;
    private Integer prepTimeMax;
    private Integer slotDuration;
    private Integer maxOrdersPerSlot;

    // ========== BEHAVIOR MỚI 1: Calculate average prep time ==========
    /**
     * Tính thời gian chuẩn bị trung bình = (min + max) / 2
     */
    public Integer calculateAveragePrepTime() {
        if (prepTimeMin == null || prepTimeMax == null) {
            return null;
        }
        return (prepTimeMin + prepTimeMax) / 2;
    }

    // ========== BEHAVIOR MỚI 2: Validation ==========
    public void validate() {
        if (prepTimeMin != null && prepTimeMax != null && prepTimeMin > prepTimeMax) {
            throw new IllegalArgumentException("prepTimeMin must be <= prepTimeMax");
        }
        if (slotDuration != null && slotDuration <= 0) {
            throw new IllegalArgumentException("slotDuration must be > 0");
        }
        if (maxOrdersPerSlot != null && maxOrdersPerSlot <= 0) {
            throw new IllegalArgumentException("maxOrdersPerSlot must be > 0");
        }
    }
}
```

### Áp dụng vào Use Cases — XÓA DUPLICATE

#### GetRestaurantDetailBySlugUseCase (dòng 79-85)

```java
// ❌ TRƯỚC
List<RestaurantPreparationSetting> prepSettings = restaurantPreparationSettingRepository
    .findByRestaurantId(restaurant.getId(), false);
Integer prepTimeAvg = null;
if (!prepSettings.isEmpty()) {
    RestaurantPreparationSetting prepSetting = prepSettings.get(0);
    prepTimeAvg = (prepSetting.getPrepTimeMin() + prepSetting.getPrepTimeMax()) / 2;
}

// ✅ SAU
List<RestaurantPreparationSetting> prepSettings = restaurantPreparationSettingRepository
    .findByRestaurantId(restaurant.getId(), false);
Integer prepTimeAvg = prepSettings.isEmpty()
    ? null
    : prepSettings.get(0).calculateAveragePrepTime();
```

#### GetRestaurantSnapshotByIdUseCase (dòng 45-51) — duplicate y hệt

```java
// ❌ TRƯỚC (duplicate)
Integer prepTimeAvg = null;
if (!prepSettings.isEmpty()) {
    RestaurantPreparationSetting prepSetting = prepSettings.get(0);
    prepTimeAvg = (prepSetting.getPrepTimeMin() + prepSetting.getPrepTimeMax()) / 2;
}

// ✅ SAU
Integer prepTimeAvg = prepSettings.isEmpty()
    ? null
    : prepSettings.get(0).calculateAveragePrepTime();
```

#### CreateRestaurantPreparationSettingUseCase

```java
// ✅ Thêm validate trước khi save
setting.validate();
RestaurantPreparationSetting saved = restaurantPreparationSettingRepository.save(setting);
```

---

## 10. RestaurantRating — Thiếu 1 Behavior

### Hiện trạng

```java
// File: domain/restaurant/model/RestaurantRating.java
@Getter
@Setter
public class RestaurantRating extends BaseEntity {
    private Long id;
    private BigDecimal ratingAvg;
    private Long restaurantId;
    private Integer ratingCount;
    // ⚠️ KHÔNG CÓ BEHAVIOR NÀO
}
```

### Code mới cho RestaurantRating

```java
// File: domain/restaurant/model/RestaurantRating.java
package com.ute.foodiedash.domain.restaurant.model;

import com.ute.foodiedash.domain.common.model.BaseEntity;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class RestaurantRating extends BaseEntity {
    private Long id;
    private BigDecimal ratingAvg;
    private Long restaurantId;
    private Integer ratingCount;

    // ========== BEHAVIOR MỚI: Null-safe rating as Double ==========
    public Double getRatingAvgAsDouble() {
        return ratingAvg != null ? ratingAvg.doubleValue() : null;
    }
}
```

### Áp dụng vào GetRestaurantDetailBySlugUseCase (dòng 56)

```java
// ❌ TRƯỚC
builder.ratingAvg(rating.getRatingAvg() != null ? rating.getRatingAvg().doubleValue() : null);

// ✅ SAU
builder.ratingAvg(rating.getRatingAvgAsDouble());
```

---

## 11. RestaurantCategory — Không cần thêm

Entity đơn giản (reference data). Không có business rule đặc biệt.

---

## 12. RestaurantCategoryMap — Không cần thêm

Entity là association table đơn giản. Không có business rule đặc biệt.

---

## 13. RestaurantImage — Không cần thêm

Entity đơn giản. Không có business rule đặc biệt.

---

## 14. Cart — Thiếu 1 Behavior

### Hiện trạng

```java
// File: domain/cart/model/Cart.java
@Getter
@Setter
public class Cart extends BaseEntity {
    private Long id;
    private Long userId;
    private Long restaurantId;
    private CartStatus status;
    private LocalDateTime expiresAt;

    public void extendExpiry() { ... }     // ✅ OK
    public boolean isExpired() { ... }     // ✅ OK
    public boolean isActive() { ... }      // ✅ OK
}
```

### Code mới cho Cart

```java
// Thêm vào Cart.java

    // ========== BEHAVIOR MỚI: Factory method ==========
    /**
     * Tạo Cart mới cho user tại restaurant cụ thể.
     * Invariant: Cart luôn bắt đầu ở ACTIVE, hết hạn sau 15 phút.
     */
    public static Cart createForUser(Long userId, Long restaurantId) {
        Cart cart = new Cart();
        cart.userId = userId;
        cart.restaurantId = restaurantId;
        cart.status = CartStatus.ACTIVE;
        cart.expiresAt = LocalDateTime.now().plusMinutes(15);
        return cart;
    }
```

### Áp dụng vào AddToCartUseCase.createCart() (dòng 160-167)

```java
// ❌ TRƯỚC
private Cart createCart(Long userId, Long restaurantId) {
    Cart cart = new Cart();
    cart.setUserId(userId);
    cart.setRestaurantId(restaurantId);
    cart.setStatus(CartStatus.ACTIVE);
    cart.setExpiresAt(LocalDateTime.now().plusMinutes(15));
    return cartRepository.save(cart);
}

// ✅ SAU
private Cart createCart(Long userId, Long restaurantId) {
    Cart cart = Cart.createForUser(userId, restaurantId);
    return cartRepository.save(cart);
}
```

---

## 15. CartItem — Thiếu 5 Behaviors

### Hiện trạng

```java
// File: domain/cart/model/CartItem.java
@Getter
@Setter
public class CartItem extends BaseEntity {
    private Long id;
    private Long cartId;
    private Long menuItemId;
    private String name;
    private String imageUrl;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
    private String notes;

    public void updateQuantity(Integer newQuantity) { ... }    // ✅ OK
    public void recalculateTotalPrice() { ... }                // ✅ OK
}
```

### Code mới cho CartItem

```java
// Thêm vào CartItem.java

    // ========== BEHAVIOR MỚI 1: Increase quantity ==========
    public void increaseQuantity() {
        updateQuantity(this.quantity + 1);
    }

    // ========== BEHAVIOR MỚI 2: Can decrease check ==========
    public boolean canDecrease() {
        return this.quantity > 1;
    }

    // ========== BEHAVIOR MỚI 3: Decrease quantity ==========
    public void decreaseQuantity() {
        if (!canDecrease()) {
            throw new IllegalStateException("Cannot decrease quantity below 1");
        }
        updateQuantity(this.quantity - 1);
    }

    // ========== BEHAVIOR MỚI 4: Update from menu item info ==========
    /**
     * Cập nhật thông tin từ menu item (name, image, price, notes).
     * Tự động recalculate total price.
     */
    public void updateFromMenuItem(String name, String imageUrl,
                                    BigDecimal unitPrice, String notes) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.unitPrice = unitPrice;
        if (notes != null) {
            this.notes = notes;
        }
        recalculateTotalPrice();
    }

    // ========== BEHAVIOR MỚI 5: Update total with extras ==========
    /**
     * Cập nhật total price khi có extra price từ options.
     *
     * @param unitTotalWithExtras giá 1 đơn vị bao gồm extras
     */
    public void updateTotalPrice(BigDecimal unitTotalWithExtras) {
        this.totalPrice = unitTotalWithExtras.multiply(BigDecimal.valueOf(quantity));
    }
```

### Áp dụng vào Use Cases

#### IncreaseCartItemQuantityUseCase (dòng 22)

```java
// ❌ TRƯỚC
cartItem.updateQuantity(cartItem.getQuantity() + 1);

// ✅ SAU
cartItem.increaseQuantity();
```

#### DecreaseCartItemQuantityUseCase (dòng 22-27)

```java
// ❌ TRƯỚC
if (cartItem.getQuantity() <= 1) {
    cartItemRepository.softDeleteById(cartItemId);
    return null;
}
cartItem.updateQuantity(cartItem.getQuantity() - 1);

// ✅ SAU
if (!cartItem.canDecrease()) {
    cartItemRepository.softDeleteById(cartItemId);
    return null;
}
cartItem.decreaseQuantity();
```

#### AddToCartUseCase — update existing item (dòng 64-72)

```java
// ❌ TRƯỚC
CartItem item = existingItem.get();
item.updateQuantity(item.getQuantity() + command.quantity());
item.setUnitPrice(menuItem.price());
item.setName(menuItem.name());
item.setImageUrl(menuItem.imageUrl());
item.setNotes(command.notes());
BigDecimal unitTotal = calculateUnitTotalPrice(menuItem, command);
item.setTotalPrice(unitTotal.multiply(BigDecimal.valueOf(item.getQuantity())));

// ✅ SAU
CartItem item = existingItem.get();
item.updateQuantity(item.getQuantity() + command.quantity());
item.updateFromMenuItem(menuItem.name(), menuItem.imageUrl(), menuItem.price(), command.notes());
BigDecimal unitTotal = calculateUnitTotalPrice(menuItem, command);
item.updateTotalPrice(unitTotal);
```

#### UpdateCartItemUseCase (dòng 54-59)

```java
// ❌ TRƯỚC
cartItem.setUnitPrice(menuItem.price());
cartItem.setName(menuItem.name());
cartItem.setImageUrl(menuItem.imageUrl());
BigDecimal unitTotal = calculateUnitTotalPrice(menuItem, command.options());
cartItem.setTotalPrice(unitTotal.multiply(BigDecimal.valueOf(cartItem.getQuantity())));

// ✅ SAU
cartItem.updateFromMenuItem(menuItem.name(), menuItem.imageUrl(), menuItem.price(), command.notes());
BigDecimal unitTotal = calculateUnitTotalPrice(menuItem, command.options());
cartItem.updateTotalPrice(unitTotal);
```

---

## 16. CartItemOption — Không cần thêm

Entity là association / snapshot data. Không có business rule đặc biệt.

---

## 17. CartItemOptionValue — Không cần thêm

Entity là association / snapshot data. Không có business rule đặc biệt.

---

## 18. Bảng Tổng Hợp

| # | Entity | Behavior hiện có | Behavior cần thêm | Use Cases bị ảnh hưởng | Mức ưu tiên |
|---|---|---|---|---|---|
| 1 | **Restaurant** | `updateSlug()`, `isActive()` | `create()`, `ensureActive()`, `assignSlug()`, `hasCoordinates()` + enum status | `CreateRestaurantUC`, `GetDetailBySlugUC`, `GetSnapshotUC` | 🔴 Cao |
| 2 | **Menu** | _(không có)_ | `create()`, `submit()`, `isDraft()`, `isActive()`, `ensureActive()`, `validate()` | `CreateMenuUC`, `SubmitMenuUC`, `AddToCartUC` | 🔴 Cao |
| 3 | **MenuItem** | _(không có)_ | `create()`, `ensureActive()`, `isActive()`, `ensureBelongsToRestaurant()`, `assignImage()` | `CreateMenuItemUC`, `AddToCartUC` | 🔴 Cao |
| 4 | **MenuItemOption** | _(không có)_ | `validateSelectionCount()`, `isRequired()` | `AddToCartUC`, `UpdateCartItemUC` | 🔴 Cao (xóa duplicate) |
| 5 | **MenuItemOptionValue** | _(không có)_ | _(không cần)_ | — | — |
| 6 | **RestaurantBusinessHour** | _(không có)_ | `isOpenAt()`, `validate()` | `RestaurantUtils`, `CreateBusinessHourUC` | 🟡 Trung bình |
| 7 | **RestaurantPause** | _(không có)_ | `isCurrentlyPaused()`, `validate()` | `CreatePauseUC` | 🟡 Trung bình |
| 8 | **RestaurantPreparationSetting** | _(không có)_ | `calculateAveragePrepTime()`, `validate()` | `GetDetailBySlugUC`, `GetSnapshotUC` | 🟡 Trung bình (xóa duplicate) |
| 9 | **RestaurantRating** | _(không có)_ | `getRatingAvgAsDouble()` | `GetDetailBySlugUC` | 🟢 Thấp |
| 10 | **RestaurantCategory** | _(không có)_ | _(không cần)_ | — | — |
| 11 | **RestaurantCategoryMap** | _(không có)_ | _(không cần)_ | — | — |
| 12 | **RestaurantImage** | _(không có)_ | _(không cần)_ | — | — |
| 13 | **Cart** | `extendExpiry()`, `isExpired()`, `isActive()` | `createForUser()` | `AddToCartUC` | 🟡 Trung bình |
| 14 | **CartItem** | `updateQuantity()`, `recalculateTotalPrice()` | `increaseQuantity()`, `decreaseQuantity()`, `canDecrease()`, `updateFromMenuItem()`, `updateTotalPrice()` | `IncreaseQtyUC`, `DecreaseQtyUC`, `AddToCartUC`, `UpdateCartItemUC` | 🔴 Cao |
| 15 | **CartItemOption** | _(không có)_ | _(không cần)_ | — | — |
| 16 | **CartItemOptionValue** | _(không có)_ | _(không cần)_ | — | — |

### Thống kê

- **Tổng entity**: 16
- **Entity cần sửa**: 10
- **Tổng behavior mới**: 29
- **Use case cần refactor**: ~12
- **Code duplicate được xóa**: 3 chỗ (~60 dòng)

---

## 19. Ưu Tiên Thực Hiện

### Phase 1: Xóa duplicate + Business rules quan trọng (Ưu tiên cao nhất)

1. `MenuItemOption.validateSelectionCount()` — xóa ~30 dòng duplicate
2. `Menu.submit()` — state machine thuộc về domain
3. `RestaurantPreparationSetting.calculateAveragePrepTime()` — xóa duplicate
4. `Restaurant` status → enum + `ensureActive()` — xóa magic string

### Phase 2: Factory methods (Đảm bảo invariants)

5. `Restaurant.create()`
6. `Menu.create()`
7. `MenuItem.create()`
8. `Cart.createForUser()`

### Phase 3: Convenience behaviors

9. `CartItem.increaseQuantity()` / `decreaseQuantity()` / `canDecrease()`
10. `CartItem.updateFromMenuItem()` / `updateTotalPrice()`
11. `RestaurantBusinessHour.isOpenAt()`
12. `RestaurantPause.isCurrentlyPaused()` / `validate()`
13. `RestaurantRating.getRatingAvgAsDouble()`

### Phase 4: Validation methods

14. `Menu.validate()`
15. `RestaurantBusinessHour.validate()`
16. `RestaurantPreparationSetting.validate()`

---

> **Lưu ý chung**: Khi thêm factory method, nên bỏ `@Setter` ở class level và thay bằng setter cho từng field cần thiết (hoặc chỉ giữ setter cho mapper). Điều này đảm bảo object không bị modify tùy ý từ bên ngoài.
