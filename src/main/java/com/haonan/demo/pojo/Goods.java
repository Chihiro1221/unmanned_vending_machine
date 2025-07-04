package com.haonan.demo.pojo;

public class Goods {
    public Goods(String id, int weight) {
        this.id = id;
        this.weight = weight;
    }

    private String id; // 6 位的商品编号，每个商品唯一
    private int weight; // 商品单件重量，单位 g

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Goods{" +
                "id='" + id + '\'' +
                ", weight=" + weight +
                '}';
    }
}