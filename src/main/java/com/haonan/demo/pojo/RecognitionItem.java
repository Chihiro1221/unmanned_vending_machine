package com.haonan.demo.pojo;

public class RecognitionItem {
    private String goodsId;
    private int num;

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "RecognitionItem{" +
                "goodsId='" + goodsId + '\'' +
                ", num=" + num +
                '}';
    }
}