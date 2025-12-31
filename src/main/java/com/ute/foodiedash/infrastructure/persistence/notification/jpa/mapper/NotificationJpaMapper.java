package com.ute.foodiedash.infrastructure.persistence.notification.jpa.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ute.foodiedash.domain.notification.enums.NotificationRole;
import com.ute.foodiedash.domain.notification.enums.NotificationType;
import com.ute.foodiedash.domain.notification.model.Notification;
import com.ute.foodiedash.infrastructure.persistence.notification.jpa.entity.NotificationJpaEntity;
import org.mapstruct.Mapper;

import java.util.Collections;
import java.util.Map;

@Mapper(componentModel = "spring")
public interface NotificationJpaMapper {
    ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    default Notification toDomain(NotificationJpaEntity e) {
        if (e == null) {
            return null;
        }

        Map<String, Object> payload = parsePayload(e.getPayload());
        return Notification.reconstruct(
                e.getId(),
                e.getRecipientUserId(),
                e.getRecipientRole(),
                e.getType(),
                e.getTitleKey(),
                e.getBodyKey(),
                payload,
                e.getDedupeKey(),
                e.getReadAt(),
                e.getCreatedAt(),
                e.getUpdatedAt(),
                e.getCreatedBy(),
                e.getUpdatedBy(),
                e.getDeletedAt(),
                e.getVersion()
        );
    }

    default NotificationJpaEntity toJpaEntity(Notification domain) {
        if (domain == null) {
            return null;
        }

        NotificationJpaEntity e = new NotificationJpaEntity();
        e.setId(domain.getId());
        e.setRecipientUserId(domain.getRecipientUserId());
        e.setRecipientRole(domain.getRecipientRole());
        e.setType(domain.getType());
        e.setTitleKey(domain.getTitleKey());
        e.setBodyKey(domain.getBodyKey());
        e.setPayload(serializePayload(domain.getPayload()));
        e.setDedupeKey(domain.getDedupeKey());
        e.setReadAt(domain.getReadAt());

        e.setCreatedAt(domain.getCreatedAt());
        e.setUpdatedAt(domain.getUpdatedAt());
        e.setCreatedBy(domain.getCreatedBy());
        e.setUpdatedBy(domain.getUpdatedBy());
        e.setDeletedAt(domain.getDeletedAt());
        e.setVersion(domain.getVersion());
        return e;
    }

    default Map<String, Object> parsePayload(String payloadJson) {
        if (payloadJson == null || payloadJson.isBlank()) {
            return Collections.emptyMap();
        }
        try {
            return OBJECT_MAPPER.readValue(payloadJson, new TypeReference<Map<String, Object>>() {});
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Cannot parse notification payload", ex);
        }
    }

    default String serializePayload(Map<String, Object> payload) {
        if (payload == null || payload.isEmpty()) {
            return null;
        }
        try {
            return OBJECT_MAPPER.writeValueAsString(payload);
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Cannot serialize notification payload", ex);
        }
    }
}

