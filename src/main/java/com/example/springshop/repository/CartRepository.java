package com.example.springshop.repository;

import com.example.springshop.entity.Cart;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CartRepository {

    private final EntityManager em;


    public Optional<Cart> findById(Long cartId) {
        return em.createQuery(
                "select c from Cart c " +
                        "left join fetch c.member " +
                        "where c.id = :cartId", Cart.class)
                .setParameter("cartId", cartId)
                .getResultList().stream().findFirst();
    }

    public Long save(Cart cart) {
        em.persist(cart);
        return cart.getId();
    }
}
