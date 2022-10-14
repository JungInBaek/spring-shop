package com.example.springshop.dto;

import com.example.springshop.entity.ItemImg;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;

@Data
public class ItemImgDto {

    private Long id;

    private String imgName;

    private String oriImgName;

    private String imgUrl;

    private String repImgYn;

    private static ModelMapper modelMapper = new ModelMapper();

    public static ItemImgDto of(ItemImg itemImg) {
        modelMapper.getConfiguration()
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE)
                .setFieldMatchingEnabled(true);
        return modelMapper.map(itemImg, ItemImgDto.class);
    }
}
