package com.ute.foodiedash;

import com.ute.foodiedash.application.order.query.Coordinate;
import com.ute.foodiedash.application.order.query.RouteQueryResult;
import com.ute.foodiedash.domain.cart.model.Cart;
import com.ute.foodiedash.domain.cart.model.CartItem;
import com.ute.foodiedash.domain.cart.model.CartItemOption;
import com.ute.foodiedash.domain.cart.model.CartItemOptionValue;
import com.ute.foodiedash.infrastructure.persistence.cart.jpa.entity.CartJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.cart.jpa.mapper.CartJpaMapper;
import com.ute.foodiedash.infrastructure.persistence.cart.jpa.repository.CartJpaRepository;
import com.ute.foodiedash.domain.menu.model.Menu;
import com.ute.foodiedash.infrastructure.persistence.menu.jpa.entity.MenuJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.menu.jpa.mapper.MenuJpaMapper;
import com.ute.foodiedash.infrastructure.persistence.menu.jpa.repository.MenuJpaRepository;
import com.ute.foodiedash.infrastructure.route.OpenRouteAdapter;
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

	@Autowired
	private MenuJpaMapper menuJpaMapper;

	@Autowired
	private MenuJpaRepository menuJpaRepository;

	@Autowired
	private OpenRouteAdapter openRouteAdapter;

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
	@Transactional
	void mapMenuAggregateToJpaEntity() {
		// given: menu aggregate (no items - separate aggregate)
		Menu menu = Menu.create(10L, "Sample menu",
			java.time.LocalTime.of(8, 0),
			java.time.LocalTime.of(22, 0));

		// when
		MenuJpaEntity jpaEntity = menuJpaMapper.toJpaEntity(menu);
		MenuJpaEntity savedJpaEntity = menuJpaRepository.save(jpaEntity);

		// then: basic fields are mapped
		assertThat(savedJpaEntity).isNotNull();
		assertThat(savedJpaEntity.getRestaurantId()).isEqualTo(menu.getRestaurantId());
		assertThat(savedJpaEntity.getName()).isEqualTo(menu.getName());
	}

	@Test
	void testCartDetail() {
		CartJpaEntity cartJpaEntity = cartJpaRepository.findCartWithDetail(6L).get();
		Cart cart = cartJpaMapper.toDomain(cartJpaEntity);
		System.out.printf("OK");
	}

	@Test
	void testMenuDetail() {
		MenuJpaEntity menuJpaEntity = menuJpaRepository.findById(1L).orElse(null);
		if (menuJpaEntity != null) {
			Menu menu = menuJpaMapper.toDomain(menuJpaEntity);
		}
	}


	@Test
	void testRoute() {
		// Coordinate record is (lat, lng)
		Coordinate a = new Coordinate(10.8499, 106.7717);
		Coordinate b = new Coordinate(10.85840, 106.76300);
		RouteQueryResult rs = openRouteAdapter.calculateRoute(a, b);
		System.out.println("OK");
	}
}
