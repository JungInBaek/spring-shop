package com.example.springshop.repository;

import com.example.springshop.dto.ItemSearchDto;
import com.example.springshop.dto.MainItemDto;
import com.example.springshop.entity.Item;
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

@Repository
@RequiredArgsConstructor
public class ItemRepositoryCustom {

    private final EntityManager em;

    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        boolean isFirstCondition = true;
        String jpql = "select i from Item i ";

        //  아이템 판매 상태 검색 조건
        if (itemSearchDto.getSearchSellStatus() != null) {
            jpql += "where i.itemSellStatus = :searchSellStatus ";
            isFirstCondition = false;
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

    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        String jpql = "select new com.example.springshop.dto.MainItemDto(i.id, i.itemName, i.itemDetail, ii.imgUrl, i.price) " +
                "from Item i " +
                "join ItemImg ii " +
                "on i.id = ii.item.id and ii.repImgYn = 'Y' ";

        String searchQuery = itemSearchDto.getSearchQuery();
        if (!StringUtils.isEmpty(searchQuery)) {
            jpql += "where i.itemName like :searchQuery ";
        }

        jpql += "order by i.id desc ";

        TypedQuery<MainItemDto> query = em.createQuery(jpql, MainItemDto.class);

        if (!StringUtils.isEmpty(searchQuery)) {
            query.setParameter("searchQuery", "%" + searchQuery + "%");
        }

        int offset = (int) pageable.getOffset();
        int pageSize = pageable.getPageSize();
        query.setFirstResult(offset).setMaxResults(pageSize);

        List<MainItemDto> content = query.getResultList();
        long total = content.size();

        return new PageImpl<>(content, pageable, total);
    }
}
