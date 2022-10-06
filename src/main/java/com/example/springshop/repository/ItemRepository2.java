package com.example.springshop.repository;

import com.example.springshop.constant.ItemSellStatus;
import com.example.springshop.dto.ItemSearchDto;
import com.example.springshop.constant.entity.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
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

    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        String jpql = "select i from Item i ";
        boolean isFirstCondition = true;

        //  아이템 판매 상태 검색 조건
        if (itemSearchDto.getSearchSellStatus() != null) {
            if (isFirstCondition) {
                jpql += "where ";
                isFirstCondition = false;
            } else {
                jpql += "and ";
            }
            jpql += "i.itemSellStatus = :searchSellStatus ";
        }

        //  날짜 검색 조건
        String searchDateType = itemSearchDto.getSearchDateType();
        LocalDateTime dateTime = LocalDateTime.now();
        if (!StringUtils.isEmpty(searchDateType) && !StringUtils.equals("all", searchDateType)) {
            if (isFirstCondition) {
                jpql += "where ";
                isFirstCondition = false;
            } else {
                jpql += "and ";
            }

            if (StringUtils.equals("1d", searchDateType)) {
                dateTime = dateTime.minusDays(1);
            } else if (StringUtils.equals("1w", searchDateType)) {
                dateTime = dateTime.minusWeeks(1);
            } else if (StringUtils.equals("1m", searchDateType)) {
                dateTime = dateTime.minusMonths(1);
            } else if (StringUtils.equals("6m", searchDateType)) {
                dateTime = dateTime.minusMonths(6);
            }
            jpql += "i.regTime >= :dateTime ";
        }

        //  상품명 or 상품 등록자 조건
        String searchBy = itemSearchDto.getSearchBy();
        String searchQuery = itemSearchDto.getSearchQuery();
        if (!StringUtils.isEmpty(searchBy) && !StringUtils.isEmpty(searchQuery)) {
            if (isFirstCondition) {
                jpql += "where ";
                isFirstCondition = false;
            } else {
                jpql += "and ";
            }

            if (StringUtils.equals("itemName", searchBy)) {
                jpql += "i.itemName like :searchQuery ";
            } else if (StringUtils.equals("createdBy", searchBy)) {
                jpql += "i.createdBy like :searchQuery ";
            }
        }

        jpql += "order by i.id asc";

        int offset = (int) pageable.getOffset();
        int pageSize = pageable.getPageSize();

        TypedQuery<Item> query = em.createQuery(jpql, Item.class);

        if (itemSearchDto.getSearchSellStatus() != null) {
            query.setParameter("searchSellStatus", itemSearchDto.getSearchSellStatus());
        }

        if ((!StringUtils.isEmpty(searchDateType) && !StringUtils.equals("all", searchDateType))) {
            query.setParameter("dateTime", dateTime);
        }

        if (!StringUtils.isEmpty(searchQuery)) {
            query.setParameter("searchQuery", "%" + searchQuery + "%");
        }

        query.setFirstResult(offset).setMaxResults(pageSize);

        List<Item> content = query.getResultList();
        long total = content.size();

        return new PageImpl<>(content, pageable, total);
    }
}
