package com.ute.foodiedash.interfaces.rest.notification;

import com.ute.foodiedash.application.notification.command.MarkNotificationsReadCommand;
import com.ute.foodiedash.application.notification.usecase.GetNotificationCountUseCase;
import com.ute.foodiedash.application.notification.usecase.ListNotificationsUseCase;
import com.ute.foodiedash.application.notification.usecase.MarkNotificationsReadUseCase;
import com.ute.foodiedash.domain.notification.enums.NotificationRole;
import com.ute.foodiedash.infrastructure.security.SecurityContextHelper;
import com.ute.foodiedash.interfaces.rest.common.dto.PageRequestDTO;
import com.ute.foodiedash.interfaces.rest.notification.dto.ListNotificationsResponseDTO;
import com.ute.foodiedash.interfaces.rest.notification.dto.MarkNotificationsReadRequestDTO;
import com.ute.foodiedash.interfaces.rest.notification.dto.MarkNotificationsReadResponseDTO;
import com.ute.foodiedash.interfaces.rest.notification.dto.NotificationCountResponseDTO;
import com.ute.foodiedash.interfaces.rest.notification.mapper.NotificationDtoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/customers/me/notifications")
@RequiredArgsConstructor
@PreAuthorize("hasAnyAuthority('ORDER_VIEW_OWN','CART_MANAGE','RESTAURANT_VIEW','RATING_CREATE')")
public class CustomerNotificationController {

    private final ListNotificationsUseCase listNotificationsUseCase;
    private final MarkNotificationsReadUseCase markNotificationsReadUseCase;
    private final GetNotificationCountUseCase notificationCountUseCase;

    private final NotificationDtoMapper notificationDtoMapper;

    private Long getCurrentCustomerId() {
        return SecurityContextHelper.getCurrentUserId();
    }

    @GetMapping
    public ResponseEntity<ListNotificationsResponseDTO> list(
            @Valid @ModelAttribute PageRequestDTO pageRequest
    ) {
        var result = listNotificationsUseCase.execute(
                getCurrentCustomerId(),
                NotificationRole.CUSTOMER,
                pageRequest.getPage(),
                pageRequest.getSize()
        );
        return ResponseEntity.ok(notificationDtoMapper.toResponseDto(result));
    }

    @GetMapping("/count")
    public ResponseEntity<NotificationCountResponseDTO> count() {
        var result = notificationCountUseCase.execute(
                getCurrentCustomerId(),
                NotificationRole.CUSTOMER
        );
        return ResponseEntity.ok(notificationDtoMapper.toResponseDto(result));
    }

    @PatchMapping("/read")
    public ResponseEntity<MarkNotificationsReadResponseDTO> markRead(
            @Valid @RequestBody MarkNotificationsReadRequestDTO request
    ) {
        long markedCount = markNotificationsReadUseCase.execute(
                new MarkNotificationsReadCommand(
                        getCurrentCustomerId(),
                        NotificationRole.CUSTOMER,
                        request.getNotificationIds()
                )
        );

        MarkNotificationsReadResponseDTO responseDTO = new MarkNotificationsReadResponseDTO();
        responseDTO.setMarkedCount(markedCount);
        return ResponseEntity.ok(responseDTO);
    }
}

