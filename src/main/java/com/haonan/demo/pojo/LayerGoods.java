package com.haonan.demo.pojo;

public class LayerGoods {
    public LayerGoods(String goodsId, int weight, int num) {
        this.goodsId = goodsId;
        this.weight = weight;
        this.num = num;
    }

    private String goodsId;
    private int weight;
    private int num;

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "LayerGoods{" +
                "goodsId='" + goodsId + '\'' +
                ", weight=" + weight +
                ", num=" + num +
                '}';
    }
}