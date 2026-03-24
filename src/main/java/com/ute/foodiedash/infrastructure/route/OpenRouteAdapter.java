package com.ute.foodiedash.infrastructure.route;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ute.foodiedash.application.order.port.RouteCalculationPort;
import com.ute.foodiedash.application.order.query.Coordinate;
import com.ute.foodiedash.application.order.query.RouteQueryResult;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OpenRouteAdapter implements RouteCalculationPort {

    private final WebClient openRouteServiceWebClient;
    private final ObjectMapper objectMapper;

    @Value("${openrouteservice.api.key}")
    private String apiKey;

    @Override
    public RouteQueryResult calculateRoute(Coordinate start, Coordinate end) {
        String response = openRouteServiceWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v2/directions/driving-car")
                        .queryParam("start", start.lng() + "," + start.lat())
                        .queryParam("end", end.lng() + "," + end.lat())
                        .build())
                .header("Authorization", apiKey)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        if (response == null || response.isBlank()) {
            return new RouteQueryResult(0.0, 0, List.of());
        }

        try {
            JsonNode root = objectMapper.readTree(response);

            JsonNode features = root.path("features");
            if (!features.isArray() || features.isEmpty()) {
                return new RouteQueryResult(0.0, 0, List.of());
            }

            JsonNode firstFeature = features.get(0);
            JsonNode properties = firstFeature.path("properties");

            JsonNode segments = properties.path("segments");
            double distance = 0.0;
            int duration = 0;
            if (segments.isArray() && !segments.isEmpty()) {
                JsonNode firstSegment = segments.get(0);
                JsonNode summary = firstSegment.path("summary");
                if (!summary.isMissingNode()) {
                    distance = summary.path("distance").asDouble(0.0);
                    duration = summary.path("duration").asInt(0);
                } else {
                    distance = firstSegment.path("distance").asDouble(0.0);
                    duration = firstSegment.path("duration").asInt(0);
                }
            }
            List<Coordinate> geometry = new ArrayList<>();
            JsonNode geometryNode = firstFeature.path("geometry").path("coordinates");
            if (geometryNode.isArray()) {
                for (JsonNode coordNode : geometryNode) {
                    if (coordNode.isArray() && coordNode.size() >= 2) {
                        BigDecimal lon = BigDecimal.valueOf(coordNode.get(0).asDouble());
                        BigDecimal lat = BigDecimal.valueOf(coordNode.get(1).asDouble());
                        geometry.add(new Coordinate(lat, lon));
                    }
                }
            }

            return new RouteQueryResult(distance, duration, geometry);
        } catch (IOException e) {
            return new RouteQueryResult(0.0, 0, List.of());
        }
    }
}
