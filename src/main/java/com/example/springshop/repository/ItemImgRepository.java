package com.example.springshop.repository;

import com.example.springshop.constant.entity.ItemImg;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ItemImgRepository {

    private final EntityManager em;


    public void save(ItemImg itemImg) {
        em.persist(itemImg);
    }

    public List<ItemImg> findByItemIdOrderByIdAsc(Long itemId) {
        return em.createQuery(
                "select ii from ItemImg ii " +
                        "join fetch ii.item " +
                        "where ii.item.id = :id " +
                        "order by ii.id asc", ItemImg.class)
                .setParameter("id", itemId)
                .getResultList();
    }

    public Optional<ItemImg> findById(Long itemImgId) {
        return em.createQuery(
                "select ii from ItemImg ii " +
                        "join fetch ii.item " +
                        "where ii.id = :id", ItemImg.class)
                .setParameter("id", itemImgId)
                .getResultStream().findAny();
    }
}
