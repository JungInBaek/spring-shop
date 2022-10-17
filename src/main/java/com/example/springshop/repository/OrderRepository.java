package com.example.springshop.repository;

import com.example.springshop.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public Long save(Order order) {
        em.persist(order);
        return order.getId();
    }

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

    public List<Order> findOrders(@Param("email") String email, Pageable pageable) {
        int offset = (int)pageable.getOffset();
        int pageSize = pageable.getPageSize();

        String jpql =
                "select o from Order o " +
                "where o.member.email = :email " +
                "order by o.orderDate desc";

        return em.createQuery(jpql, Order.class)
                .setParameter("email", email)
                .setFirstResult(offset)
                .setMaxResults(pageSize)
                .getResultList();
    }

    public Long countOrder(@Param("email") String email) {
        String jpql =
                "select count(o) from Order o " +
                "where o.member.email = :email";

        return em.createQuery(jpql, Long.class)
                .setParameter("email", email)
                .getSingleResult();
    }

}
