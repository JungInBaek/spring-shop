package com.example.springshop.repository;

import com.example.springshop.dto.CartDetailDto;
import com.example.springshop.dto.CartItemDto;
import com.example.springshop.entity.CartItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
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

    public List<CartDetailDto> findCartDetailDtoList(Long cartId) {
        String jpql = "select new com.example.springshop.dto.CartDetailDto(ci.id, i.itemName, i.price, ci.count, ii.imgUrl) " +
                "from CartItem ci, ItemImg ii " +
                "join ci.item i " +
                "where ci.cart.id = :cartId " +
                "and ii.item.id = ci.item.id " +
                "and ii.repImgYn = 'Y' " +
                "order by ci.regTime desc";

        return em.createQuery(jpql, CartDetailDto.class)
                .setParameter("cartId", cartId)
                .getResultList();
    }

    public void delete(CartItem cartItem) {
        em.remove(cartItem);
    }
}
