package com.example.springshop.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    private int orderPrice;

    private int count;

    protected OrderItem(Item item, int count) {
        this.item = item;
        this.count = count;
    }

    protected OrderItem(Order order, Item item, int orderPrice, int count) {
        this.order = order;
        this.item = item;
        this.orderPrice = orderPrice;
        this.count = count;
    }

    public int getTotalPrice() {
        return orderPrice * count;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    //  테스트용
    public static OrderItem createOrderItem(Order order, Item item, int orderPrice, int count) {
        return new OrderItem(order, item, orderPrice, count);
    }

    public static OrderItem createOrderItem(Item item, int count) {
        OrderItem orderItem = new OrderItem(item, count);
        orderItem.orderPrice = item.getPrice();
        item.removeStock(count);

        return orderItem;
    }

    public void cancel() {
        this.getItem().addStock(count);
    }
}
