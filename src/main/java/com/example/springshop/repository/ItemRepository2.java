package com.example.springshop.repository;

import com.example.springshop.entity.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ItemRepository2 {

    private final EntityManager em;

    public Item save(Item item) {
        em.persist(item);
        return em.find(Item.class, item.getId());
    }

    public Item findOne(Long itemId) {
        return em.find(Item.class, itemId);
    }

    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }

    public Optional<Item> findById(Long itemId) {
        return Optional.of(em.find(Item.class, itemId));
    }

    public List<Item> findByItemName(String itemName) {
        return em.createQuery("select i from Item i where i.itemName = :itemName", Item.class)
                .setParameter("itemName", itemName)
                .getResultList();
    }

    public List<Item> findByItemNameOrItemDetail(String itemName, String itemDetail) {
        return em.createQuery("select i from Item i where i.itemName = :itemName or i.itemDetail = :itemDetail", Item.class)
                .setParameter("itemName", itemName)
                .setParameter("itemDetail", itemDetail)
                .getResultList();
    }

    public List<Item> findByPriceLessThan(Integer price) {
        return em.createQuery("select i from Item i where i.price < :price", Item.class)
                .setParameter("price", price)
                .getResultList();
    }

    public List<Item> findByPriceLessThanOrderByPriceDesc(Integer price) {
        return em.createQuery("select i from Item i where i.price < :price order by i.price desc", Item.class)
                .setParameter("price", price)
                .getResultList();
    }

    public List<Item> findByItemDetail(String itemDetail) {
        return em.createQuery("select i from Item i where i.itemDetail like :itemDetail order by i.price desc", Item.class)
                .setParameter("itemDetail", "%" + itemDetail + "%")
                .getResultList();
    }

    public List<Item> findByItemDetailByNative(String itemDetail) {
        return em.createNativeQuery("select * from item i where i.item_detail like :itemDetail order by i.price desc", Item.class)
                .setParameter("itemDetail", "%" + itemDetail + "%")
                .getResultList();
    }
}
