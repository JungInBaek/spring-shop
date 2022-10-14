package com.example.springshop.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "item_img")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemImg {

    @Id @GeneratedValue
    @Column(name = "item_img_id")
    private Long id;

    private String imgName;

    private String oriImgName;

    private String imgUrl;

    private String repImgYn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    protected ItemImg(Item item) {
        this.item = item;
    }

    public static ItemImg createItemImg(Item item) {
        return new ItemImg(item);
    }

    public void updateItemImg(String oriImgName, String imgName, String imgUrl) {
        this.oriImgName = oriImgName;
        this.imgName = imgName;
        this.imgUrl = imgUrl;
    }

    public void setRepImgYn(String repImgYn) {
        this.repImgYn = repImgYn;
    }
}
