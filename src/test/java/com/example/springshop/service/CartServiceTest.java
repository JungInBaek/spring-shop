package com.example.springshop.service;

import com.example.springshop.constant.ItemSellStatus;
import com.example.springshop.dto.CartItemDto;
import com.example.springshop.entity.CartItem;
import com.example.springshop.entity.Item;
import com.example.springshop.entity.Member;
import com.example.springshop.repository.CartItemRepository;
import com.example.springshop.repository.ItemRepository;
import com.example.springshop.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class CartServiceTest {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    CartService cartService;

    @Autowired
    CartItemRepository cartItemRepository;


    public Item saveItem() {
        Item item = Item.createItem("테스트 상품", 10000, 100, "테스트 상품 상세 설명", ItemSellStatus.SELL);
        return itemRepository.save(item);
    }

    public Member saveMember() {
        Member member = Member.createMember("test@test.com");
        return memberRepository.save(member);
    }


    @Test
    @DisplayName("장바구니 담기 테스트")
    public void addCart() {
        Item item = saveItem();
        Member member = saveMember();

        CartItemDto cartItemDto = new CartItemDto(item.getId(), 5);
        Long cartItemId = cartService.addCart(cartItemDto, member.getEmail());
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(EntityNotFoundException::new);

        assertEquals(item.getId(), cartItem.getItem().getId());
        assertEquals(cartItemDto.getCount(), cartItem.getCount());
    }
}