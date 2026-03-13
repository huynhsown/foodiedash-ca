package com.ute.foodiedash;

import com.ute.foodiedash.domain.cart.model.Cart;
import com.ute.foodiedash.domain.cart.model.CartItem;
import com.ute.foodiedash.domain.cart.model.CartItemOption;
import com.ute.foodiedash.domain.cart.model.CartItemOptionValue;
import com.ute.foodiedash.infrastructure.persistence.cart.jpa.entity.CartJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.cart.jpa.mapper.CartJpaMapper;
import com.ute.foodiedash.infrastructure.persistence.cart.jpa.repository.CartJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class FoodiedashApplicationTests {

	@Autowired
	private CartJpaMapper cartJpaMapper;

	@Autowired
	private CartJpaRepository cartJpaRepository;

	@Test
	@Transactional
	@Commit
	void mapCartAggregateToJpaEntity() {
		// given: a cart aggregate with one item and options
		Cart cart = Cart.createForUser(1L, 10L);

		CartItem item = CartItem.create(
			100L,
			"Sample item",
			"https://example.com/image.png",
			2,
			new BigDecimal("50000"),
			"no onions"
		);

		CartItemOption sizeOption = CartItemOption.create(
			200L,
			"Size",
			true,
			1,
			1
		);

		CartItemOptionValue largeSize = CartItemOptionValue.create(
			201L,
			"Large",
			1,
			new BigDecimal("10000")
		);

		sizeOption.addValue(largeSize);
		sizeOption.validateSelection();

		item.addOption(sizeOption);
		cart.addItem(item);

		// when
		CartJpaEntity jpaEntity = cartJpaMapper.toJpaEntity(cart);
		CartJpaEntity savedJpaEntity  = cartJpaRepository.save(jpaEntity);
		cartJpaRepository.flush();

		// then: basic fields are mapped
		assertThat(jpaEntity).isNotNull();
		assertThat(jpaEntity.getUserId()).isEqualTo(cart.getUserId());
		assertThat(jpaEntity.getRestaurantId()).isEqualTo(cart.getRestaurantId());
	}

	@Test
	void testCartDetail() {
		CartJpaEntity cartJpaEntity = cartJpaRepository.findCartWithDetail(6L).get();
		Cart cart = cartJpaMapper.toDomain(cartJpaEntity);
		System.out.printf("OK");
	}

}
