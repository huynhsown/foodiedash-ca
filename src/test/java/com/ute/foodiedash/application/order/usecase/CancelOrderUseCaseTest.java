package com.ute.foodiedash.application.order.usecase;

import com.ute.foodiedash.application.order.command.CancelOrderCommand;
import com.ute.foodiedash.application.order.query.OrderSummaryQueryResult;
import com.ute.foodiedash.application.order.port.OrderCustomerNotificationPort;
import com.ute.foodiedash.domain.common.exception.BadRequestException;
import com.ute.foodiedash.domain.common.exception.ForbiddenException;
import com.ute.foodiedash.domain.common.exception.NotFoundException;
import com.ute.foodiedash.domain.order.enums.OrderStatus;
import com.ute.foodiedash.domain.order.model.Order;
import com.ute.foodiedash.domain.order.model.OrderPromotion;
import com.ute.foodiedash.domain.order.model.OrderStatusHistory;
import com.ute.foodiedash.domain.order.repository.OrderRepository;
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
class CancelOrderUseCaseTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderCustomerNotificationPort orderCustomerNotificationPort;

    @InjectMocks
    private CancelOrderUseCase useCase;

    @Test
    void execute_shouldCancelOrder_whenOwnerMatches() {
        Long customerId = 10L;
        Long orderId = 99L;

        Order order = Order.reconstruct(
                orderId,
                "ORD-99",
                customerId,
                20L,
                OrderStatus.PENDING,
                new BigDecimal("100000"),
                BigDecimal.ZERO,
                new BigDecimal("15000"),
                new BigDecimal("115000"),
                LocalDateTime.now(),
                null,
                null,
                null,
                null,
                null,
                List.of(),
                List.<OrderPromotion>of(),
                List.<OrderStatusHistory>of(),
                null,
                null,
                null,
                null,
                null,
                0L
        );

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(org.mockito.Mockito.any())).thenAnswer(inv -> inv.getArgument(0));

        CancelOrderCommand command = new CancelOrderCommand(orderId, "Customer request");
        OrderSummaryQueryResult result = useCase.execute(customerId, command);

        assertThat(result.orderId()).isEqualTo(orderId);
        assertThat(result.status()).isEqualTo(OrderStatus.CANCELLED.name());
        assertThat(result.cancelledAt()).isNotNull();
    }

    @Test
    void execute_shouldThrowNotFound_whenOrderMissing() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(10L, new CancelOrderCommand(1L, "reason")))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Order not found");
    }

    @Test
    void execute_shouldThrowForbidden_whenOwnerMismatch() {
        Order order = Order.reconstruct(
                1L,
                "ORD-1",
                999L,
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
                List.<OrderPromotion>of(),
                List.<OrderStatusHistory>of(),
                null,
                null,
                null,
                null,
                null,
                0L
        );
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertThatThrownBy(() -> useCase.execute(10L, new CancelOrderCommand(1L, "reason")))
                .isInstanceOf(ForbiddenException.class)
                .hasMessage("You are not allowed to cancel this order");
    }

    @Test
    void execute_shouldThrowBadRequest_whenInvalidStateTransition() {
        Order order = Order.reconstruct(
                1L,
                "ORD-1",
                10L,
                20L,
                OrderStatus.COMPLETED,
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
                List.<OrderPromotion>of(),
                List.<OrderStatusHistory>of(),
                null,
                null,
                null,
                null,
                null,
                0L
        );
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertThatThrownBy(() -> useCase.execute(10L, new CancelOrderCommand(1L, "reason")))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Only pending or accepted orders can be cancelled");
    }
}

