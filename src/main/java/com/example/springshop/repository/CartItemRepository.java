package com.example.springshop.repository;

import com.example.springshop.entity.CartItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.NoSuchElementException;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CartItemRepository {

    private final EntityManager em;


    public void save(CartItem cartItem) {
        em.persist(cartItem);
    }

    public CartItem findByCartIdAndItemId(Long cartId, Long itemId) {
        try {
            return em.createQuery(
                            "select ci from CartItem ci " +
                                    "join fetch ci.item " +
                                    "where ci.cart.id = :cartId " +
                                    "and ci.item.id = :itemId", CartItem.class)
                    .setParameter("cartId", cartId)
                    .setParameter("itemId", itemId)
                    .getResultStream().findAny().get();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    public Optional<CartItem> findById(Long cartItemId) {
        return em.createQuery(
                        "select ci from CartItem ci " +
                                "where ci.id = :cartItemId", CartItem.class)
                .setParameter("cartItemId", cartItemId)
                .getResultStream().findAny();
    }

}
