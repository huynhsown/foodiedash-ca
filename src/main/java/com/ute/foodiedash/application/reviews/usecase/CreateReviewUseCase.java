package com.ute.foodiedash.application.reviews.usecase;

import com.ute.foodiedash.application.order.port.OrderValidationPort;
import com.ute.foodiedash.application.order.port.model.OrderValidation;
import com.ute.foodiedash.application.restaurant.port.ImageUploadPort;
import com.ute.foodiedash.application.reviews.command.CreateReviewCommand;
import com.ute.foodiedash.application.reviews.query.ReviewQueryResult;
import com.ute.foodiedash.domain.common.exception.BadRequestException;
import com.ute.foodiedash.domain.order.enums.OrderStatus;
import com.ute.foodiedash.domain.order.model.Order;
import com.ute.foodiedash.domain.order.repository.OrderRepository;
import com.ute.foodiedash.domain.reviews.model.Review;
import com.ute.foodiedash.domain.reviews.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CreateReviewUseCase {
    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;
    private final ImageUploadPort imageUploadPort;
    private final OrderValidationPort orderValidationPort;

    @Transactional
    public ReviewQueryResult execute(CreateReviewCommand command, List<MultipartFile> images) {
//        Order order = orderRepository.findById(command.orderId()).orElseThrow(
//                () -> new BadRequestException("Order not found")
//        );
//
//        if (!order.isBelongToCustomer(command.customerId())) {
//            throw new BadRequestException("Not your order");
//        }
//
//        if (!order.isCompleted()) {
//            throw new BadRequestException("Cannot review uncompleted order");
//        }
        if (images.size() > 5) {
            throw new BadRequestException("You can upload up to 5 images");
        }

        OrderValidation orderValidation = orderValidationPort.findForReview(command.orderId())
                .orElseThrow(() -> new BadRequestException("Order not found"));

        if (!orderValidation.customerId().equals(command.customerId())) {
            throw new BadRequestException("Not your order");
        }

        if (orderValidation.status() != OrderStatus.COMPLETED) {
            throw new BadRequestException("Cannot review uncompleted order");
        }

        if (reviewRepository.existsByOrderIdAndCustomerId(command.orderId(), command.customerId())) {
            throw new BadRequestException("You have already reviewed this order");
        }

        List<String> imageURLs = images.stream()
                .map(image -> {
                    try {
                        Map<String, Object> result =
                                imageUploadPort.uploadImage(image, "review-images");
                        return result.get("secure_url").toString();
                    } catch (IOException e) {
                        throw new RuntimeException("Upload failed", e);
                    }
                })
                .toList();

        Review review = Review.create(orderValidation.customerId(), orderValidation.orderId(),
                orderValidation.restaurantId(), command.rating(),
                command.comment(), imageURLs);

        review = reviewRepository.save(review);

        return ReviewQueryResult.from(review);
    }
}
