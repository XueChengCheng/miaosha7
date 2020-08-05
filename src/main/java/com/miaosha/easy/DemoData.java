package com.miaosha.easy;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class DemoData {
    @ExcelProperty("商品id")
    private int goodsID;
    @ExcelProperty("商品名称")
    private String goodsName;
    @ExcelProperty("商品标题")
    private String goodsTitle;
    @ExcelProperty("商品图片")
    private String goodsImage;
    @ExcelProperty("商品细节")
    private String goodsDetail;
    @ExcelProperty("商品价格")
     private Double goodsPrice;

    /**
     * 忽略这个字段
     */
    @ExcelIgnore
    private int goodsStock;
}