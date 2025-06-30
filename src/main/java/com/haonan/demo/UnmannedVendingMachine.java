package com.haonan.demo;

import com.haonan.demo.enums.ExceptionEnum;
import com.haonan.demo.pojo.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 无人售货机入口类
 *
 * @author haonan
 */
public class UnmannedVendingMachine {

    private final static int MAX_LAYER = 10;
    private static final int MIN_WEIGHT = 0;
    private static final int MAX_WEIGHT = 32767;

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
    public static RecognitionResult recognize(List<Layer> openLayers,
                                              List<Layer> closeLayers,
                                              List<Goods> goodsList,
                                              List<Stock> stockList,
                                              int sensorTolerance) {
        // 构造初始化
        RecognitionResult recognitionResult = new RecognitionResult();
        recognitionResult.setItems(new ArrayList<RecognitionItem>());
        recognitionResult.setExceptions(new ArrayList<RecognitionException>());
        // 转换成map方便操作
        Map<Integer, Integer> openLayersMap = openLayers.stream().collect(Collectors.toMap(Layer::getIndex, Layer::getWeight));
        Map<Integer, Integer> closeLayersMap = closeLayers.stream().collect(Collectors.toMap(Layer::getIndex, Layer::getWeight));
        Map<String, Integer> goodsMap = goodsList.stream().collect(Collectors.toMap(Goods::getId, Goods::getWeight));
        // 遍历每一层，当计算出商品组合后放入结果
        for (int i = 1; i <= MAX_LAYER; ++i) {
            Integer beginWeight = openLayersMap.get(i);
            Integer endWeight = closeLayersMap.get(i);
            // 当前层重量约束判断
            if (!checkWeight(beginWeight) || !checkWeight(endWeight)) {
                RecognitionException recognitionException = new RecognitionException();
                recognitionException.setLayer(i);
                recognitionException.setBeginWeight(beginWeight == null ? -1 : beginWeight);
                recognitionException.setEndWeight(endWeight == null ? -1 : endWeight);
                recognitionException.setException(ExceptionEnum.SENSOR_ERROR);
                recognitionResult.getExceptions().add(recognitionException);
                continue;
            }
            // 放入异物约束
            if (endWeight > beginWeight) {
                RecognitionException recognitionException = new RecognitionException();
                recognitionException.setLayer(i);
                recognitionException.setBeginWeight(beginWeight == null ? -1 : beginWeight);
                recognitionException.setEndWeight(endWeight == null ? -1 : endWeight);
                recognitionException.setException(ExceptionEnum.FOREIGN_OBJECT);
                continue;
            }
            // 没有变化
            if (endWeight.equals(beginWeight)) {
                continue;
            }

            // 拿到当前层的库存列表
            int finalI = i;
            List<Stock> curStockList = stockList.stream().filter(stock -> stock.getLayer() == finalI).toList();

            recognizeLayerGoods(recognitionResult, beginWeight, endWeight, stockList, goodsMap);


        }
        return recognitionResult;
    }

    /**
     * 识别当前层用户拿的商品组合
     *
     * @param recognitionResult
     * @param beginWeight
     * @param endWeight
     * @param stockList
     * @param goodsMap
     */
    private static void recognizeLayerGoods(RecognitionResult recognitionResult, Integer beginWeight, Integer endWeight, List<Stock> stockList, Map<String, Integer> goodsMap) {

    }

    /**
     * 检测层重量是否符合约束
     *
     * @param weight
     * @return
     */
    private static boolean checkWeight(Integer weight) {
        return weight != null && weight >= MIN_WEIGHT && weight <= MAX_WEIGHT;
    }

}
