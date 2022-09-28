package com.example.springshop.repository;

import com.example.springshop.entity.OrderItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderItemRepository {

    private final EntityManager em;


    public Optional<OrderItem> findById(Long orderItemId) {
        return em.createQuery(
                "select oi from OrderItem oi " +
                        "join oi.order o " +
                "where oi.id = :id", OrderItem.class)
                .setParameter("id", orderItemId)
                .getResultList().stream().findAny();
    }
}
