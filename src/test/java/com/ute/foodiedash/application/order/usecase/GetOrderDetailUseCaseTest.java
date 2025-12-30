package com.ute.foodiedash.application.order.usecase;

import com.ute.foodiedash.application.order.query.OrderDetailQueryResult;
import com.ute.foodiedash.domain.common.exception.NotFoundException;
import com.ute.foodiedash.domain.common.exception.UnauthorizedException;
import com.ute.foodiedash.domain.order.enums.OrderStatus;
import com.ute.foodiedash.domain.order.model.Order;
import com.ute.foodiedash.domain.order.model.OrderDelivery;
import com.ute.foodiedash.domain.order.model.OrderPayment;
import com.ute.foodiedash.domain.order.enums.PaymentStatus;
import com.ute.foodiedash.domain.order.repository.OrderDeliveryRepository;
import com.ute.foodiedash.domain.order.repository.OrderPaymentRepository;
import com.ute.foodiedash.domain.order.repository.OrderRepository;
import com.ute.foodiedash.domain.paymentmethod.enums.PaymentMethodCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetOrderDetailUseCaseTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderPaymentRepository orderPaymentRepository;
    @Mock
    private OrderDeliveryRepository orderDeliveryRepository;

    @InjectMocks
    private GetOrderDetailUseCase useCase;

    @Test
    void execute_shouldReturnOrderDetail_whenOwnerMatches() {
        Long customerId = 10L;
        Long orderId = 99L;

        Order order = Order.reconstruct(
                orderId,
                "ORD-99",
                customerId,
                20L,
                OrderStatus.PENDING,
                new BigDecimal("100000"),
                new BigDecimal("10000"),
                new BigDecimal("15000"),
                new BigDecimal("105000"),
                LocalDateTime.now(),
                null,
                null,
                null,
                null,
                null,
                List.of(),
                List.of(),
                List.of(),
                null,
                null,
                null,
                null,
                null,
                null
        );

        OrderPayment payment = OrderPayment.reconstruct(
                1L,
                orderId,
                PaymentMethodCode.VNPAY,
                PaymentStatus.PAID,
                "TXN-1",
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                0L
        );
        OrderDelivery delivery = OrderDelivery.reconstruct(
                2L,
                orderId,
                null,
                "123 ABC",
                new BigDecimal("10.7"),
                new BigDecimal("106.6"),
                "Son",
                "0909",
                "Giao nhanh",
                new BigDecimal("3.2"),
                15,
                List.of(),
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                0L
        );

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderPaymentRepository.findByOrderId(orderId)).thenReturn(Optional.of(payment));
        when(orderDeliveryRepository.findByOrderId(orderId)).thenReturn(Optional.of(delivery));

        OrderDetailQueryResult result = useCase.execute(customerId, orderId);

        assertThat(result.orderId()).isEqualTo(orderId);
        assertThat(result.orderCode()).isEqualTo("ORD-99");
        assertThat(result.payment()).isNotNull();
        assertThat(result.delivery()).isNotNull();
    }

    @Test
    void execute_shouldThrowNotFound_whenOrderDoesNotExist() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(10L, 1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Order not found");
    }

    @Test
    void execute_shouldThrowUnauthorized_whenOrderBelongsToOtherCustomer() {
        Order order = Order.reconstruct(
                1L,
                "ORD-1",
                99L,
                20L,
                OrderStatus.PENDING,
                BigDecimal.ONE,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ONE,
                LocalDateTime.now(),
                null,
                null,
                null,
                null,
                null,
                List.of(),
                List.of(),
                List.of(),
                null,
                null,
                null,
                null,
                null,
                null
        );
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertThatThrownBy(() -> useCase.execute(10L, 1L))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage("You are not allowed to access this order");
    }
}
