package com.example.springshop.repository;

import com.example.springshop.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    public Member findOne(Long memberId) {
        return em.find(Member.class, memberId);
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public Member findByEmail(String email) {
        try {
            return em.createQuery("select m from Member m where m.email = :email", Member.class)
                    .setParameter("email", email)
                    .getResultList().stream().findAny().get();
        } catch(NoSuchElementException e) {
            return null;
        }
    }

    public Optional<Member> findById(Long memberId) {
        Member member = em.createQuery(
                        "select m from Member m " +
                                "where m.id = :id", Member.class)
                .setParameter("id", memberId)
                .getResultList().get(0);
        return Optional.of(member);
    }
}
