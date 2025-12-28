package com.ute.foodiedash.interfaces.rest.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ute.foodiedash.application.order.query.OrderDetailQueryResult;
import com.ute.foodiedash.application.order.usecase.CancelOrderUseCase;
import com.ute.foodiedash.application.order.usecase.CheckoutOrderUseCase;
import com.ute.foodiedash.application.order.usecase.CompleteOrderUseCase;
import com.ute.foodiedash.application.order.usecase.GetOrderDetailUseCase;
import com.ute.foodiedash.application.order.usecase.GetOrdersByCustomerUseCase;
import com.ute.foodiedash.application.order.usecase.PreviewOrderUseCase;
import com.ute.foodiedash.domain.common.exception.NotFoundException;
import com.ute.foodiedash.interfaces.exception.GlobalExceptionHandler;
import com.ute.foodiedash.interfaces.rest.order.dto.OrderDetailResponseDTO;
import com.ute.foodiedash.interfaces.rest.order.mapper.CancelOrderDtoMapper;
import com.ute.foodiedash.interfaces.rest.order.mapper.OrderDetailDtoMapper;
import com.ute.foodiedash.interfaces.rest.order.mapper.OrderMapper;
import com.ute.foodiedash.interfaces.rest.order.mapper.OrderSummaryDtoMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
@Import(GlobalExceptionHandler.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CheckoutOrderUseCase checkoutOrderUseCase;

    @MockBean
    private PreviewOrderUseCase previewOrderUseCase;

    @MockBean
    private GetOrderDetailUseCase getOrderDetailUseCase;

    @MockBean
    private GetOrdersByCustomerUseCase getOrdersByCustomerUseCase;

    @MockBean
    private CancelOrderUseCase cancelOrderUseCase;

    @MockBean
    private CompleteOrderUseCase completeOrderUseCase;

    @MockBean
    private OrderMapper orderMapper;

    @MockBean
    private OrderDetailDtoMapper orderDetailDtoMapper;

    @MockBean
    private OrderSummaryDtoMapper orderSummaryDtoMapper;

    @MockBean
    private CancelOrderDtoMapper cancelOrderDtoMapper;

    @Test
    void getOrderDetail_shouldReturn200_withResponseShape() throws Exception {
        Long orderId = 123L;

        OrderDetailQueryResult queryResult = new OrderDetailQueryResult(
                orderId,
                "ODR-123",
                "PENDING",
                new BigDecimal("100000"),
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                new BigDecimal("100000"),
                LocalDateTime.now(),
                null,
                null,
                null,
                List.of(),
                List.of(),
                List.of(),
                null,
                null
        );

        OrderDetailResponseDTO responseDTO = new OrderDetailResponseDTO();
        responseDTO.setOrderId(orderId);
        responseDTO.setOrderCode("ODR-123");
        responseDTO.setStatus("PENDING");
        responseDTO.setSubtotal(100000L);
        responseDTO.setDiscount(0L);
        responseDTO.setDeliveryFee(0L);
        responseDTO.setTotal(100000L);

        Mockito.when(getOrderDetailUseCase.execute(Mockito.anyLong(), Mockito.eq(orderId)))
                .thenReturn(queryResult);
        Mockito.when(orderDetailDtoMapper.toResponseDto(Mockito.eq(queryResult)))
                .thenReturn(responseDTO);

        mockMvc.perform(get("/api/v1/orders/{orderId}", orderId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(orderId))
                .andExpect(jsonPath("$.orderCode").value("ODR-123"))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.subtotal").value(100000))
                .andExpect(jsonPath("$.discount").value(0))
                .andExpect(jsonPath("$.deliveryFee").value(0))
                .andExpect(jsonPath("$.total").value(100000));
    }

    @Test
    void getOrderDetail_shouldReturn404_whenOrderNotFound() throws Exception {
        Long orderId = 999L;
        Mockito.when(getOrderDetailUseCase.execute(Mockito.anyLong(), Mockito.eq(orderId)))
                .thenThrow(new NotFoundException("Order not found"));

        mockMvc.perform(get("/api/v1/orders/{orderId}", orderId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Order not found"));
    }
}

