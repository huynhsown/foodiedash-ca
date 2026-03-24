package com.ute.foodiedash.infrastructure.persistence.promotion.jpa.entity;

import com.ute.foodiedash.domain.cart.model.Cart;
import com.ute.foodiedash.domain.cart.model.CartItem;
import com.ute.foodiedash.domain.cart.model.CartItemOption;
import com.ute.foodiedash.domain.cart.model.CartItemOptionValue;
import com.ute.foodiedash.domain.cart.repository.CartRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@SpringBootTest
class PromotionJpaEntityTest {
    @Autowired
    private CartRepository cartRepository;

    @Test
    @Transactional
    @Commit
    void shouldPersistPromotionWithAllFields() {
        Cart cart = Cart.createForUser(1L, 2L);
        cart.setExpiresAt(LocalDateTime.of(2027, 1, 1, 0, 0));

        CartItem item = CartItem.create(100L, "Test Item", "http://example.com/image.jpg", 2, new BigDecimal("50000"), "No onions");

        CartItemOption option = CartItemOption.create(10L, "Size", true, 1, 1);
        CartItemOptionValue optionValue = CartItemOptionValue.create(1L, "Large", 1, new BigDecimal("10000"));
        option.addValue(optionValue);
        item.addOption(option);

        CartItem item2 = CartItem.create(101L, "Test Item 2", "http://example.com/image2.jpg", 1, new BigDecimal("30000"), "Extra spicy");

        CartItemOption option2 = CartItemOption.create(11L, "Ice Level", false, 0, 3);
        CartItemOptionValue optionValue2 = CartItemOptionValue.create(2L, "Less Ice", 1, new BigDecimal("0"));
        option2.addValue(optionValue2);
        item2.addOption(option2);

        cart.addItem(item);
        cart.addItem(item2);

        cartRepository.save(cart);
    }

    @Test
    @Transactional
    @Commit
    void testMapper() {
        Cart cart = cartRepository.findById(6L).orElseThrow();
        cart.decreaseItemQuantity(7L);
        cartRepository.save(cart);
    }
}
