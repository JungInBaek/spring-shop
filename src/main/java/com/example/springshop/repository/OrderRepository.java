package com.example.springshop.repository;

import com.example.springshop.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void saveAndFlush(Order order) {
        em.persist(order);
        em.flush();
    }

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    public Optional<Order> findById(Long id) {
        Order order = em.createQuery(
                        "select o from Order o " +
//                                "join o.orderItems oi " +
                                "where o.id = :id", Order.class)
                .setParameter("id", id)
                .getResultList().get(0);
        return Optional.of(order);
    }

    public Long save(Order order) {
        em.persist(order);
        return order.getId();
    }
}
