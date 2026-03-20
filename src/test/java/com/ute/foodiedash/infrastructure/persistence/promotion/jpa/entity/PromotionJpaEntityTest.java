package com.ute.foodiedash.infrastructure.persistence.promotion.jpa.entity;

import com.ute.foodiedash.domain.cart.enums.CartStatus;
import com.ute.foodiedash.domain.cart.model.Cart;
import com.ute.foodiedash.domain.cart.repository.CartRepository;
import com.ute.foodiedash.infrastructure.persistence.cart.jpa.entity.CartItemJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.cart.jpa.entity.CartItemOptionJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.cart.jpa.entity.CartItemOptionValueJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.cart.jpa.entity.CartJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.cart.jpa.mapper.CartJpaMapper;
import com.ute.foodiedash.infrastructure.persistence.cart.jpa.repository.CartJpaRepository;
import lombok.RequiredArgsConstructor;
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
    private CartJpaRepository cartJpaRepository;
    @Autowired
    private CartRepository cartRepository;

    @Test
    @Transactional
    @Commit
    void shouldPersistPromotionWithAllFields() {
        CartJpaEntity cart = new CartJpaEntity();
        cart.setUserId(1L);
        cart.setRestaurantId(1L);
        cart.setStatus(CartStatus.ACTIVE);
        cart.setExpiresAt(LocalDateTime.of(2027, 1, 1, 0, 0));

        CartItemJpaEntity item = new CartItemJpaEntity();
        item.setMenuItemId(100L);
        item.setName("Test Item");
        item.setImageUrl("http://example.com/image.jpg");
        item.setQuantity(2);
        item.setUnitPrice(new BigDecimal("50000"));
        item.setTotalPrice(new BigDecimal("100000"));
        item.setNotes("No onions");
        item.setCart(cart);

        CartItemOptionJpaEntity option = new CartItemOptionJpaEntity();
        option.setOptionId(10L);
        option.setOptionName("Size");
        option.setRequired(true);
        option.setMinValue(1);
        option.setMaxValue(1);
        option.setCartItem(item);

        CartItemOptionValueJpaEntity optionValue = new CartItemOptionValueJpaEntity();
        optionValue.setOptionValueId(1L);
        optionValue.setOptionValueName("Large");
        optionValue.setQuantity(1);
        optionValue.setExtraPrice(new BigDecimal("10000"));
        optionValue.setCartItemOption(option);

        option.getValues().add(optionValue);
        item.getOptions().add(option);
        cart.getItems().add(item);

        CartItemJpaEntity item2 = new CartItemJpaEntity();
        item2.setMenuItemId(101L);
        item2.setName("Test Item 2");
        item2.setImageUrl("http://example.com/image2.jpg");
        item2.setQuantity(1);
        item2.setUnitPrice(new BigDecimal("30000"));
        item2.setTotalPrice(new BigDecimal("30000"));
        item2.setNotes("Extra spicy");
        item2.setCart(cart);

        CartItemOptionJpaEntity option2 = new CartItemOptionJpaEntity();
        option2.setOptionId(11L);
        option2.setOptionName("Ice Level");
        option2.setRequired(false);
        option2.setMinValue(0);
        option2.setMaxValue(3);
        option2.setCartItem(item2);

        CartItemOptionValueJpaEntity optionValue2 = new CartItemOptionValueJpaEntity();
        optionValue2.setOptionValueId(2L);
        optionValue2.setOptionValueName("Less Ice");
        optionValue2.setQuantity(1);
        optionValue2.setExtraPrice(new BigDecimal("0"));
        optionValue2.setCartItemOption(option2);

        option2.getValues().add(optionValue2);
        item2.getOptions().add(option2);
        cart.getItems().add(item2);

        CartJpaEntity saved = cartJpaRepository.save(cart);
        cartJpaRepository.flush();
    }

    @Test
    @Transactional
    @Commit
    void testMapper() {
        Cart cart = cartRepository.findById(3L).orElseThrow();
        cart.decreaseItemQuantity(4L);
        cartRepository.save(cart);
    }
}
