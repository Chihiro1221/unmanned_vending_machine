package com.haonan.demo.pojo;

public class Layer {
    private int index; // 编号，从 1 开始
    private int weight; // 重量传感器数值，单位 g

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}