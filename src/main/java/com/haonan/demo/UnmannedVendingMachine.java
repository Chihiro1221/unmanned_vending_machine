package com.haonan.demo;

import com.haonan.demo.pojo.Goods;
import com.haonan.demo.pojo.Layer;
import com.haonan.demo.pojo.RecognitionResult;
import com.haonan.demo.pojo.Stock;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 无人售货机入口类
 *
 * @author haonan
 */
public class UnmannedVendingMachine {

    /**
     * 取货识别
     *
     * @param openLayers
     * @param closeLayers
     * @param goodsList
     * @param stockList
     * @param sensorTolerance
     * @return
     */
    public RecognitionResult recognize(List<Layer> openLayers,
                                       List<Layer> closeLayers,
                                       List<Goods> goodsList,
                                       List<Stock> stockList,
                                       int sensorTolerance) {
        RecognitionResult recognitionResult = new RecognitionResult();
        return recognitionResult;
    }

}
