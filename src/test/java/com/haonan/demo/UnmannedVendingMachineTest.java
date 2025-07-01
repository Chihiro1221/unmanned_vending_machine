package com.haonan.demo;

import com.haonan.demo.pojo.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.haonan.demo.UnmannedVendingMachine.MAX_WEIGHT;
import static com.haonan.demo.UnmannedVendingMachine.MIN_WEIGHT;
import static org.junit.jupiter.api.Assertions.*;

class UnmannedVendingMachineTest {
    List<Goods> goodsList = Arrays.asList(
            // 可乐：ID=100001，重量=300g
            new Goods("100001", 300),
            // 薯片：ID=100002，重量=150g
            new Goods("100002", 150),
            // 巧克力：ID=100003，重量=80g
            new Goods("100003", 80),
            // 矿泉水：ID=100004，重量=500g
            new Goods("100004", 500),
            // 饼干：ID=100005，重量=200g
            new Goods("100005", 200),
            // 能量饮料：ID=100006，重量=330g
            new Goods("100006", 330)
    );
    List<Stock> stockList = Arrays.asList(
            // 第1层：可乐5瓶
            new Stock("100001", 1, 5),
            // 第1层：薯片3袋（同一层多种商品）
            new Stock("100002", 1, 3),
            // 第2层：巧克力8块
            new Stock("100003", 2, 8),
            // 第3层：矿泉水4瓶
            new Stock("100004", 3, 4),
            // 第4层：饼干6盒
            new Stock("100005", 4, 6),
            // 第5层：能量饮料5罐
            new Stock("100006", 5, 5),
            // 第6层：无商品（空层）
            // 第7层：可乐3瓶（与第1层重复）
            new Stock("100001", 7, 3),
            // 第8层：薯片5袋（与第1层重复）
            new Stock("100002", 8, 5),
            // 第9层：矿泉水2瓶
            new Stock("100004", 9, 2),
            // 第10层：能量饮料3罐（与第5层商品重复）
            new Stock("100006", 10, 3)
    );

    // 开门时各层重量
    List<Layer> openLayers = Arrays.asList(
            new Layer(1, 1950),  // 5瓶可乐(1500g) + 3袋薯片(450g) = 1950g
            new Layer(2, 640),   // 8块巧克力(640g)
            new Layer(3, 2000),  // 4瓶矿泉水(2000g)
            new Layer(4, 1200),  // 6盒饼干(1200g)
            new Layer(5, 1650),  // 5罐能量饮料(1650g)
            new Layer(6, 0),     // 空层
            new Layer(7, 900),   // 3瓶可乐(900g)
            new Layer(8, 750),   // 5袋薯片(750g)
            new Layer(9, 1000),  // 2瓶矿泉水(1000g)
            new Layer(10, 990)   // 3罐能量饮料(990g)
    );

    // 关门时各层重量
    List<Layer> closeLayers = Arrays.asList(
            new Layer(1, 150),  // 1袋薯片(150g) 少了5瓶可乐+2袋薯片
            new Layer(2, 640),   // 无变化
            new Layer(3, 2000),  // 无变化
            new Layer(4, 1200),  // 无变化
            new Layer(5, 1320),  // 4瓶能量饮料(330*4) 少了1瓶能量饮料
            new Layer(6, 0),     // 无变化
            new Layer(7, 600),   // 2瓶可乐(300*2) 少了1瓶可乐
            new Layer(8, 750),   // 无变化
            new Layer(9, 1000),  // 无变化
            new Layer(10, 660)   // 2瓶能量饮料(330*2) 少了1瓶能量饮料
    );


    /**
     * 初步跑通流程
     * 预取结果：6瓶可乐，2袋薯片，2瓶能量饮料
     * 结果中，从不同层拿不同商品可以类加起来计算，不会重复
     */
    @Test
    void recognize() {
        RecognitionResult recognize = UnmannedVendingMachine.recognize(openLayers, closeLayers, goodsList, stockList, 10);
        Assertions.assertNotNull(recognize);
        System.out.println(recognize);
    }

    /**
     * 测试传感器误差边界
     * 预取结果：第一层出现异常，因为范围变成了[1650, 1900]，存在多种方案，其余层拿到1瓶可乐，2瓶能量饮料
     */
    @Test
    void recognizeWithTolerance() {
        RecognitionResult recognize = UnmannedVendingMachine.recognize(openLayers, closeLayers, goodsList, stockList, 150);
        Assertions.assertNotNull(recognize);
        System.out.println(recognize);
    }

    /**
     * 测试传感器误差边界方案 2
     * - 1层取一瓶可乐，但是开关门重量差设置成140，然后加上10的传感器误差就可以计算出来
     * - 如果将传感器误差设置成8，则计算不出来取了1瓶可乐
     */
    @Test
    void recognizeWithTolerance2() {
        // 关门时各层重量
        List<Layer> closeLayers = Arrays.asList(
                new Layer(1, 1810),  // 5瓶可乐+2袋薯片(1800) 少了一袋薯片(150g)，测试写成140g
                new Layer(2, 640),   // 无变化
                new Layer(3, 2000),  // 无变化
                new Layer(4, 1200),  // 无变化
                new Layer(5, 1320),  // 4瓶能量饮料(330*4) 少了1瓶能量饮料
                new Layer(6, 0),     // 无变化
                new Layer(7, 600),   // 2瓶可乐(300*2) 少了1瓶可乐
                new Layer(8, 750),   // 无变化
                new Layer(9, 1000),  // 无变化
                new Layer(10, 660)   // 2瓶能量饮料(330*2) 少了1瓶能量饮料
        );
        RecognitionResult recognize = UnmannedVendingMachine.recognize(openLayers, closeLayers, goodsList, stockList, 10);
        Assertions.assertNotNull(recognize);
        System.out.println(recognize);
    }

    /**
     * 测试传感器第1层与最后10层重量不在正常取值范围内
     */
    @Test
    void recognizeWithSensor_error() {
        Layer first = openLayers.getFirst();
        Layer last = closeLayers.getLast();
        first.setWeight(MIN_WEIGHT - 1);
        last.setWeight(MAX_WEIGHT + 1);
        RecognitionResult recognize = UnmannedVendingMachine.recognize(openLayers, closeLayers, goodsList, stockList, 10);
        Assertions.assertNotNull(recognize);
        System.out.println(recognize);
    }

    /**
     * 测试放入异物异常情况
     */
    @Test
    void recognizeWithForeign_object() {
        Layer openFirst = openLayers.getFirst();
        Layer closeFirst = closeLayers.getFirst();
        openFirst.setWeight(1000);
        closeFirst.setWeight(2000);
        RecognitionResult recognize = UnmannedVendingMachine.recognize(openLayers, closeLayers, goodsList, stockList, 10);
        Assertions.assertNotNull(recognize);
        System.out.println(recognize);
    }

    /**
     * 测试识别函数
     * 预设场景：用户拿了2包干脆面，2包辣条，1桶泡面
     * 执行结果：包含这个商品组合
     */
    @Test
    void recognizeLayerGoods() {
        // 总重量124
        LayerGoods layerGood = new LayerGoods("干脆面", 8, 3);
        LayerGoods layerGood2 = new LayerGoods("泡面", 12, 5);
        LayerGoods layerGood3 = new LayerGoods("辣条", 4, 10);
        List<LayerGoods> layerGoods = List.of(layerGood, layerGood2, layerGood3);
        List<List<RecognitionItem>> lists = UnmannedVendingMachine.recognizeLayerGoods(layerGoods, 124, 88, 10);
        Assertions.assertNotNull(lists);
        System.out.println(lists);
    }
}