package com.haonan.demo;

import com.haonan.demo.pojo.Goods;
import com.haonan.demo.pojo.Layer;
import com.haonan.demo.pojo.RecognitionResult;
import com.haonan.demo.pojo.Stock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

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
}