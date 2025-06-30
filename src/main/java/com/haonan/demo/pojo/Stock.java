package com.haonan.demo.pojo;

public class Stock {
    private String goodsId; // 库存对应的商品
    private int layer; // 库存对应的层架
    private int num; // 库存数量

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}