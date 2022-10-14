package com.example.springshop.entity;

import com.example.springshop.constant.ItemSellStatus;
import com.example.springshop.dto.ItemFormDto;
import com.example.springshop.exception.OutOfStockException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item extends BaseEntity {
    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;                        //  상품 코드

    @Column(nullable = false, length = 50)
    private String itemName;                  //  상품명

    @Column(nullable = false)
    private int price;                      //  가격

    @Column(nullable = false)
    private int stockNumber;                //  재고 수량

    @Lob
    @Column(nullable = false)
    private String itemDetail;              //  상품 상세 설명

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus;  //  상품 판매 상태

//    private LocalDateTime regTime;          //  등록 시간
//
//    private LocalDateTime updateTime;       //  수정 시간

    protected Item(String itemName, int price, int stockNumber, String itemDetail,
                   ItemSellStatus itemSellStatus) {
        this.itemName = itemName;
        this.price = price;
        this.stockNumber = stockNumber;
        this.itemDetail = itemDetail;
        this.itemSellStatus = itemSellStatus;
//        this.regTime = regTime;
//        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", itemName='" + itemName + '\'' +
                ", price=" + price +
                ", stockNumber=" + stockNumber +
                ", itemDetail='" + itemDetail + '\'' +
                ", itemSellStatus=" + itemSellStatus +
//                ", regTime=" + regTime +
//                ", updateTime=" + updateTime +
                '}';
    }

    public static Item createItem(String itemName, int price, int stockNumber, String itemDetail,
                                  ItemSellStatus itemSellStatus) {
        return new Item(itemName, price, stockNumber, itemDetail, itemSellStatus);
    }

    public void updateItem(ItemFormDto itemFormDto) {
        this.itemName = itemFormDto.getItemName();
        this.price = itemFormDto.getPrice();
        this.stockNumber = itemFormDto.getStockNumber();
        this.itemDetail = itemFormDto.getItemDetail();
        this.itemSellStatus = itemFormDto.getItemSellStatus();
    }

    public void removeStock(int stockNumber) {
        int restStock = this.stockNumber - stockNumber;
        if (restStock < 0) {
            throw new OutOfStockException("상품의 재고가 부족합니다. (현재 재고 수량: " + this.stockNumber + ")");
        }
        this.stockNumber = restStock;
    }
}
