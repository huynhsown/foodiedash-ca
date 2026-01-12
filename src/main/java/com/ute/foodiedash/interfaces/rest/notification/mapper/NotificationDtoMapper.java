package com.ute.foodiedash.interfaces.rest.notification.mapper;

import com.ute.foodiedash.application.notification.query.ListNotificationsQueryResult;
import com.ute.foodiedash.application.notification.query.NotificationCountQueryResult;
import com.ute.foodiedash.application.notification.query.NotificationResult;
import com.ute.foodiedash.interfaces.rest.notification.dto.ListNotificationsResponseDTO;
import com.ute.foodiedash.interfaces.rest.notification.dto.NotificationCountResponseDTO;
import com.ute.foodiedash.interfaces.rest.notification.dto.NotificationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NotificationDtoMapper {

    NotificationDTO toDto(NotificationResult result);

    ListNotificationsResponseDTO toResponseDto(ListNotificationsQueryResult result);

    List<NotificationDTO> toDtos(List<NotificationResult> results);

    NotificationCountResponseDTO toResponseDto(NotificationCountQueryResult result);
}

