package com.example.springshop.dto;

import com.example.springshop.constant.ItemSellStatus;
import lombok.Data;

@Data
public class ItemSearchDto {

    private String searchDateType;

    private ItemSellStatus searchSellStatus;

    private String searchBy;

    private String searchQuery = "";
}
