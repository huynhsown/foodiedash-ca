package com.ute.foodiedash.infrastructure.persistence.order.jpa.mapper;

import com.ute.foodiedash.domain.order.model.Order;
import com.ute.foodiedash.domain.order.model.OrderItem;
import com.ute.foodiedash.domain.order.model.OrderItemOption;
import com.ute.foodiedash.domain.order.model.OrderItemOptionValue;
import com.ute.foodiedash.infrastructure.persistence.order.jpa.entity.OrderItemJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.order.jpa.entity.OrderItemOptionJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.order.jpa.entity.OrderItemOptionValueJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.order.jpa.entity.OrderJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.order.jpa.repository.OrderJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class OrderJpaMapperTest {

    @Autowired
    private OrderJpaMapper orderJpaMapper;

    @Autowired
    private OrderJpaRepository orderJpaEntityRepository;

    @Test
    @Transactional
    @Commit
    void toJpaEntity_shouldMapNestedItemsAndSetReferences() {
        Order order = Order.create("ODR-1001", 1L, 1L, new BigDecimal("15000"));
        OrderItem item = OrderItem.create(
            100L,
            "Beef Burger",
            "https://example.com/burger.png",
            2,
            new BigDecimal("50000"),
            "No onion"
        );
        OrderItemOption option = OrderItemOption.create(200L, "Size", true, 1, 1);
        OrderItemOptionValue value = OrderItemOptionValue.create(
            201L,
            "Large",
            1,
            new BigDecimal("10000")
        );
        option.addValue(value);
        option.validateSelection();
        item.addOption(option);

        OrderItem item2 = OrderItem.create(
            101L,
            "French Fries",
            "https://example.com/fries.png",
            1,
            new BigDecimal("30000"),
            "Less salt"
        );
        OrderItemOption option2 = OrderItemOption.create(202L, "Sauce", false, 0, 2);
        OrderItemOptionValue value2 = OrderItemOptionValue.create(
            203L,
            "BBQ Sauce",
            1,
            new BigDecimal("5000")
        );
        option2.addValue(value2);
        option2.validateSelection();
        item2.addOption(option2);

        order.addItem(item);
        order.addItem(item2);
        order.setDiscountAmount(new BigDecimal("5000"));

        OrderJpaEntity jpa = orderJpaMapper.toJpaEntity(order);

        assertThat(jpa).isNotNull();
        assertThat(jpa.getCode()).isEqualTo("ODR-1001");
        assertThat(jpa.getItems()).hasSize(2);

        OrderItemJpaEntity itemJpa = jpa.getItems().get(0);
        assertThat(itemJpa.getOrder()).isSameAs(jpa);
        assertThat(itemJpa.getOptions()).hasSize(1);

        OrderItemOptionJpaEntity optionJpa = itemJpa.getOptions().get(0);
        assertThat(optionJpa.getOrderItem()).isSameAs(itemJpa);
        assertThat(optionJpa.getValues()).hasSize(1);

        OrderItemOptionValueJpaEntity valueJpa = optionJpa.getValues().get(0);
        assertThat(valueJpa.getOrderItemOption()).isSameAs(optionJpa);

        OrderItemJpaEntity itemJpa2 = jpa.getItems().get(1);
        assertThat(itemJpa2.getOrder()).isSameAs(jpa);
        assertThat(itemJpa2.getOptions()).hasSize(1);
        OrderItemOptionJpaEntity optionJpa2 = itemJpa2.getOptions().get(0);
        assertThat(optionJpa2.getOrderItem()).isSameAs(itemJpa2);
        assertThat(optionJpa2.getValues()).hasSize(1);
        OrderItemOptionValueJpaEntity valueJpa2 = optionJpa2.getValues().get(0);
        assertThat(valueJpa2.getOrderItemOption()).isSameAs(optionJpa2);

        OrderJpaEntity saved = orderJpaEntityRepository.save(jpa);
        orderJpaEntityRepository.flush();

        assertThat(saved.getId()).isNotNull();

        OrderJpaEntity persisted = orderJpaEntityRepository.findDetailById(saved.getId()).orElseThrow();
        assertThat(persisted.getCode()).isEqualTo("ODR-1001");
        assertThat(persisted.getItems()).hasSize(2);
        assertThat(persisted.getItems().get(0).getOptions()).hasSize(1);
        assertThat(persisted.getItems().get(1).getOptions()).hasSize(1);
    }

    @Test
    @Transactional
    void toDomain_shouldMapNestedStructure() {
        OrderJpaEntity orderJpa = orderJpaEntityRepository.findDetailById(7L).get();

        Order domain = orderJpaMapper.toDomain(orderJpa);

        assertThat(domain).isNotNull();
        assertThat(domain.getId()).isEqualTo(7L);
        assertThat(domain.getCode()).isEqualTo("ODR-1001");
        assertThat(domain.getItems()).hasSize(2);

        OrderItem domainItem1 = domain.getItems().get(0);
        assertThat(domainItem1.getOptions()).hasSize(1);
        OrderItemOption domainOption1 = domainItem1.getOptions().get(0);
        assertThat(domainOption1.getValues()).hasSize(1);
        OrderItemOptionValue domainValue1 = domainOption1.getValues().get(0);
        assertThat(domainValue1.getOptionValueName()).isEqualTo("Extra cheese");

        OrderItem domainItem2 = domain.getItems().get(1);
        assertThat(domainItem2.getOptions()).hasSize(1);
        OrderItemOption domainOption2 = domainItem2.getOptions().get(0);
        assertThat(domainOption2.getValues()).hasSize(1);
        OrderItemOptionValue domainValue2 = domainOption2.getValues().get(0);
        assertThat(domainValue2.getOptionValueName()).isEqualTo("BBQ Sauce");
    }

    @Test
    void test() {
        orderJpaEntityRepository.findDetailById(1L);
        System.out.println("OK");
    }
}
