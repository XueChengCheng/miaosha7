package com.miaosha.vo;

import java.util.Date;



public class GoodsVo{

	//商城的商品表
	private Long id;
	private String goodsName;
	private String goodsTitle;
	private String goodsImg;
	private String goodsDetail;
	private Double goodsPrice;   
	private Integer goodsStock;
	
	//秒杀商品
	private Long goodsId;
	private Integer stockCount;
	private Date startDate;
	private Date endDate;
	private Double miaoshaPrice;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getGoodsTitle() {
		return goodsTitle;
	}
	public void setGoodsTitle(String goodsTitle) {
		this.goodsTitle = goodsTitle;
	}
	public String getGoodsImg() {
		return goodsImg;
	}
	public void setGoodsImg(String goodsImg) {
		this.goodsImg = goodsImg;
	}
	public String getGoodsDetail() {
		return goodsDetail;
	}
	public void setGoodsDetail(String goodsDetail) {
		this.goodsDetail = goodsDetail;
	}
	public Double getGoodsPrice() {
		return goodsPrice;
	}
	public void setGoodsPrice(Double goodsPrice) {
		this.goodsPrice = goodsPrice;
	}
	public Integer getGoodsStock() {
		return goodsStock;
	}
	public void setGoodsStock(Integer goodsStock) {
		this.goodsStock = goodsStock;
	}
	public Long getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}
	public Integer getStockCount() {
		return stockCount;
	}
	public void setStockCount(Integer stockCount) {
		this.stockCount = stockCount;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Double getMiaoshaPrice() {
		return miaoshaPrice;
	}
	public void setMiaoshaPrice(Double miaoshaPrice) {
		this.miaoshaPrice = miaoshaPrice;
	}
	@Override
	public String toString() {
		return "GoodsVo [id=" + id + ", goodsName=" + goodsName + ", goodsTitle=" + goodsTitle + ", goodsImg="
				+ goodsImg + ", goodsDetail=" + goodsDetail + ", goodsPrice=" + goodsPrice + ", goodsStock="
				+ goodsStock + ", goodsId=" + goodsId + ", stockCount=" + stockCount + ", startDate=" + startDate
				+ ", endDate=" + endDate + ", miaoshaPrice=" + miaoshaPrice + "]";
	}   
	
	 
	
	
}
