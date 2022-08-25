package com.example.springshop.repository;

import com.example.springshop.constant.ItemSellStatus;
import com.example.springshop.entity.Item;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @Test
    @DisplayName("상품 저장 테스트")
    public void createItemTest() throws Exception {
        //  given
        Item item = new Item();
        item.setItemName("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("테스트 상품 상세 설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        item.setRegTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());

        //  when
        Item savedItem = itemRepository.save(item);

        //  then
        assertThat(savedItem).isEqualTo(item);
    }

    private void createItemList() {
        for (int i = 0; i < 10; i++) {
            Item item = new Item();
            item.setItemName("테스트 상품" + i);
            item.setPrice(10000 + i);
            item.setItemDetail("테스트 상품 상세 설명" + i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockNumber(100);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            itemRepository.save(item);
        }
    }

    @Test
    @DisplayName("상품명 조회 테스트")
    public void findByItemNameTest() throws Exception {
        //  given
        this.createItemList();

        //  when
        List<Item> itemList = itemRepository.findByItemName("테스트 상품1");

        //  then
        assertThat(itemList.size()).isEqualTo(1);
        assertThat(itemList.get(0).getItemName()).isEqualTo("테스트 상품1");
    }

    @Test
    @DisplayName("상품명, 상품상세설명 or 테스트")
    public void findByItemNameOrItemDetailTest() throws Exception {
        //  given
        this.createItemList();

        //  when
        List<Item> itemList = itemRepository.findByItemNameOrItemDetail("테스트 상품1", "테스트 상품 상세 설명5");

        //  then
        assertThat(itemList.size()).isEqualTo(2);
        assertThat(itemList.get(0).getItemName()).isEqualTo("테스트 상품1");
        assertThat(itemList.get(1).getItemName()).isEqualTo("테스트 상품5");
    }

    @Test
    @DisplayName("가격 LessThan 테스트")
    public void findByPriceLessThanTest() throws Exception {
        //  given
        this.createItemList();

        //  when
        List<Item> itemList = itemRepository.findByPriceLessThan(10005);

        //  then
        assertThat(itemList.size()).isEqualTo(5);
    }

    @Test
    @DisplayName("가격 내림차순 조회 테스트")
    public void findByPriceLessThanOrderByPriceDesc() throws Exception {
        //  given
        this.createItemList();

        //  when
        List<Item> itemList = itemRepository.findByPriceLessThanOrderByPriceDesc(10005);
        for (Item item : itemList) {
            System.out.println("item = " + item);
        }

        //  then
        assertThat(itemList.size()).isEqualTo(5);
    }
}