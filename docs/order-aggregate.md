## Order Aggregate - Business Logic

### 1. Tổng quan aggregate

- **Aggregate root**: `Order`
- **Các entity con**:
  - `OrderItem`: đại diện cho từng món trong đơn hàng.
  - `OrderItemOption`: đại diện cho một nhóm tùy chọn của từng món (ví dụ: kích cỡ, topping, loại sốt).
  - `OrderItemOptionValue`: đại diện cho từng giá trị tùy chọn được chọn trong một nhóm (ví dụ: size lớn, thêm phô mai).
- **Mối quan hệ**:
  - Một `Order` có nhiều `OrderItem` (`items`).
  - Một `OrderItem` có nhiều `OrderItemOption` (`options`).
  - Một `OrderItemOption` có nhiều `OrderItemOptionValue` (`values`).

### 2. Order

**Thuộc tính chính**

- **Thông tin định danh & liên kết**
  - `id`: định danh đơn hàng.
  - `code`: mã đơn hàng (hiển thị cho khách hàng, dùng để tra cứu).
  - `customerId`: khách hàng đặt đơn.
  - `restaurantId`: nhà hàng xử lý đơn.
  - `status` (`OrderStatus`): trạng thái đơn (ví dụ: PENDING, ACCEPTED, PREPARING, COMPLETED, CANCELLED, ...).

- **Thông tin giá tiền**
  - `subtotalAmount`: tổng tiền hàng (chưa áp dụng khuyến mãi, phí giao hàng).
  - `discountAmount`: tổng số tiền giảm giá áp dụng cho đơn.
  - `deliveryFee`: phí giao hàng.
  - `totalAmount`: tổng thanh toán cuối cùng (= subtotal − discount + deliveryFee).

- **Thông tin thời gian & hủy đơn**
  - `placedAt`: thời điểm khách đặt đơn.
  - `acceptedAt`: thời điểm nhà hàng chấp nhận đơn.
  - `preparedAt`: thời điểm nhà hàng bắt đầu/hoàn tất chuẩn bị món (tùy cách dùng).
  - `cancelledAt`: thời điểm đơn bị hủy.
  - `cancelReason`: lý do hủy đơn.

- **Quan hệ**
  - `items`: danh sách `OrderItem` thuộc đơn hàng.

**Ý tưởng logic nghiệp vụ chính (dựa trên cấu trúc hiện tại)**

- **Quản lý trạng thái đơn hàng**
  - Khi tạo đơn:
    - `status` ban đầu thường là `PENDING` (hoặc tương đương).
    - `placedAt` được gán thời điểm hiện tại.
  - Khi nhà hàng chấp nhận:
    - Cập nhật `status` sang `ACCEPTED`.
    - Gán `acceptedAt` thời điểm hiện tại.
  - Khi nhà hàng bắt đầu/hoàn tất chuẩn bị:
    - Cập nhật `status` sang `PREPARING` hoặc `COMPLETED` (tùy trạng thái thiết kế).
    - Gán `preparedAt`.
  - Khi hủy đơn:
    - Cập nhật `status` sang `CANCELLED`.
    - Gán `cancelledAt` và `cancelReason` (bắt buộc có lý do hợp lệ).

- **Quy tắc tính tiền cấp đơn hàng**
  - `subtotalAmount` được tính dựa trên:
    - Tổng `totalPrice` của tất cả `OrderItem`.
  - `discountAmount` phản ánh tất cả khuyến mãi áp dụng lên đơn:
    - Mã giảm giá, khuyến mãi nhà hàng, khuyến mãi hệ thống, v.v.
  - `deliveryFee` phụ thuộc:
    - Khoảng cách giao hàng, cấu hình phí cố định, chương trình miễn phí ship, v.v.
  - `totalAmount` phải đảm bảo:
    - Luôn không âm.
    - Phù hợp với công thức: `totalAmount = subtotalAmount - discountAmount + deliveryFee`.

> Lưu ý: hiện tại class `Order` mới dừng ở mức cấu trúc dữ liệu (chưa có method domain), nhưng đây là nơi nên đặt các quy tắc:
> - Thêm/xóa/sửa `OrderItem`.
> - Recalculate subtotal/total khi item thay đổi.
> - Chuyển trạng thái đơn (accept, prepare, cancel, complete) kèm validation hợp lệ.

### 3. OrderItem

**Thuộc tính chính**

- `id`: định danh dòng món.
- `orderId`: tham chiếu về `Order` cha.
- `menuItemId`: tham chiếu về món trong menu tại thời điểm đặt.

- **Thông tin hiển thị**
  - `name`: tên món tại thời điểm đặt (snapshot, tránh bị ảnh hưởng khi menu đổi).
  - `imageUrl`: hình ảnh món tại thời điểm đặt.

- **Thông tin giá & số lượng**
  - `quantity`: số lượng món.
  - `unitPrice`: đơn giá cơ bản (chưa tính option).
  - `totalPrice`: tổng tiền dòng món (bao gồm base price và phần extra của options).

- **Khác**
  - `notes`: ghi chú của khách (ví dụ: ít đá, không hành).
  - `options`: danh sách `OrderItemOption` áp dụng lên món này.

**Ý tưởng logic nghiệp vụ cấp dòng món**

- Khi tạo `OrderItem`:
  - Phải có `menuItemId`, `name`, `unitPrice`, `quantity > 0`.
  - `totalPrice` nên được tính theo:
    - \( totalPrice = (unitPrice + tổng extra từ options) \times quantity \).
- Khi thay đổi `quantity` hoặc thêm/bớt `OrderItemOptionValue`:
  - `totalPrice` của item cần được cập nhật lại.
  - `Order` cha phải cập nhật lại `subtotalAmount` và `totalAmount`.
- Không cho phép:
  - `quantity <= 0`.
  - `totalPrice` âm.

### 4. OrderItemOption

**Thuộc tính chính**

- `id`: định danh nhóm tùy chọn.
- `orderItemId`: tham chiếu về `OrderItem` cha.
- `optionId`: tham chiếu về option gốc trên menu (ví dụ “Size”, “Topping”).
- `optionName`: tên option tại thời điểm đặt (snapshot).
- `required`: cho biết nhóm tùy chọn này có bắt buộc chọn hay không.
- `minValue`: số lượng giá trị tối thiểu phải chọn.
- `maxValue`: số lượng giá trị tối đa được chọn.
- `values`: danh sách `OrderItemOptionValue` đã chọn.

**Ý tưởng logic nghiệp vụ cấp option**

- Khi thêm/chỉnh sửa `values`:
  - Số lượng `values` (hoặc tổng `quantity` tùy thiết kế) phải nằm trong khoảng \[minValue, maxValue\] nếu `minValue`/`maxValue` khác `null`.
  - Nếu `required = true`, phải đảm bảo:
    - Có ít nhất `minValue` lựa chọn (hoặc ít nhất 1 nếu `minValue` `null`).
- Việc vi phạm các ràng buộc trên nên dẫn tới exception ở domain (ví dụ `BadRequestException`).

> Hiện tại class `OrderItemOption` mới là data structure, chưa có method domain. Những validation trên là ứng viên để triển khai trong tương lai.

### 5. OrderItemOptionValue

Đây là entity duy nhất hiện tại đã có logic nghiệp vụ cụ thể.

**Thuộc tính chính**

- `id`: định danh giá trị tùy chọn (ở cấp đơn hàng).
- `orderItemOptionId`: tham chiếu về `OrderItemOption` cha.
- `optionValueId`: tham chiếu về giá trị option gốc trên menu.
- `optionValueName`: tên giá trị option tại thời điểm đặt (snapshot).
- `quantity`: số lượng (ví dụ: thêm 2 lần phô mai).
- `extraPrice`: phụ thu cho mỗi đơn vị của option.

**Logic nghiệp vụ trong code**

- **Tạo mới**: `OrderItemOptionValue.create(...)`

  Quy tắc:

  - `optionValueId` bắt buộc, nếu `null` → ném `BadRequestException("Option value id required")`.
  - `optionValueName` bắt buộc, không được `null` hoặc rỗng → ném `BadRequestException("Option value name required")`.
  - `quantity` bắt buộc và phải > 0:
    - Nếu `quantity == null` hoặc `quantity <= 0` → ném `BadRequestException("Quantity must be positive")`.
  - `extraPrice`:
    - Nếu `extraPrice == null` → mặc định `BigDecimal.ZERO`.
    - Nếu `extraPrice < 0` → ném `BadRequestException("Extra price must be positive")`.
  - Sau khi validation:
    - Gán các trường tương ứng và trả về instance mới.

- **Khôi phục từ persistence**: `OrderItemOptionValue.reconstruct(...)`

  Mục đích:

  - Dùng để tái tạo entity từ dữ liệu persistence (JPA, database) về domain.
  - Thiết lập tất cả trường định danh, liên kết, dữ liệu, và metadata audit.

  Hành vi:

  - Gán:
    - `id`, `orderItemOptionId`, `optionValueId`, `optionValueName`, `quantity`, `extraPrice`.
  - Gọi `restoreAudit(...)` kế thừa từ `BaseEntity`:
    - Khôi phục các trường: `createdAt`, `updatedAt`, `createdBy`, `updatedBy`, `deletedAt`, `version`.

- **Tính tổng phụ thu cho option value**: `getTotalExtraPrice()`

  - Công thức:
    - \( totalExtraPrice = extraPrice \times quantity \).
  - Đảm bảo:
    - Không làm thay đổi trạng thái entity (pure function trên dữ liệu nội tại).

### 6. Tổng hợp business logic cấp aggregate

Dựa trên cấu trúc hiện tại và logic có sẵn ở `OrderItemOptionValue`, aggregate `Order` dự kiến sẽ bao gồm các luật sau (một phần sẽ được triển khai dần trong domain):

- **Toàn vẹn dữ liệu theo cây aggregate**
  - Mọi thay đổi ở `OrderItemOptionValue` → ảnh hưởng `OrderItem` (giá dòng món) → ảnh hưởng `Order` (subtotal/total).
  - Không cho phép ở trạng thái “đã hoàn tất/đã hủy” mà vẫn thay đổi dữ liệu chi tiết món.

- **Tính toán giá**
  - Ở cấp option value:
    - Tính `getTotalExtraPrice()` cho mỗi `OrderItemOptionValue`.
  - Ở cấp option:
    - Tổng extra price = tổng `getTotalExtraPrice()` của tất cả `values`.
  - Ở cấp item:
    - `totalPrice = (unitPrice + tổng extra price từ tất cả options) * quantity`.
  - Ở cấp order:
    - `subtotalAmount = tổng totalPrice của tất cả items`.
    - `totalAmount = subtotalAmount - discountAmount + deliveryFee`.

- **Quản lý trạng thái & vòng đời đơn hàng**
  - Chỉ cho phép:
    - Chuyển từ `PENDING` → `ACCEPTED` / `CANCELLED`.
    - Từ `ACCEPTED` → `PREPARING` → `COMPLETED` hoặc `CANCELLED`.
    - Từ `COMPLETED` không được quay lại trạng thái trước.
  - Hủy đơn (`CANCELLED`) phải có `cancelReason`, và tùy chính sách:
    - Có thể bị giới hạn theo thời gian (không cho phép hủy sau khi đã chuẩn bị xong).

> Phần lớn các quy tắc trên hiện chưa được mã hóa vào method domain ở các class `Order`, `OrderItem`, `OrderItemOption`, nhưng đây là định hướng hợp lý để tiếp tục hoàn thiện aggregate theo Clean Architecture.

