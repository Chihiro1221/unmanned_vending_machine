package com.haonan.demo;

import com.haonan.demo.pojo.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UnmannedVendingMachineTest {

    @Test
    void recognize() {
        UnmannedVendingMachine unmannedVendingMachine = new UnmannedVendingMachine();
        ArrayList<Layer> openLayers = new ArrayList<>();
        ArrayList<Layer> closeLayers = new ArrayList<>();
        ArrayList<Goods> goodsList = new ArrayList<>();
        ArrayList<Stock> stocksList = new ArrayList<>();
        RecognitionResult recognize = unmannedVendingMachine.recognize(openLayers, closeLayers, goodsList, stocksList, 10);
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
        List<List<RecognitionItem>> lists = UnmannedVendingMachine.recognizeLayerGoods(layerGoods, 124, 88);
        Assertions.assertNotNull(lists);
        System.out.println(lists);
    }
}