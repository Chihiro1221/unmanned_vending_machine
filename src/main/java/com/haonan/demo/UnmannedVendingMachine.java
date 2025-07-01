package com.haonan.demo;

import com.haonan.demo.enums.ExceptionEnum;
import com.haonan.demo.pojo.*;

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
        // 初始化结果类
        RecognitionResult recognitionResult = new RecognitionResult();
        recognitionResult.setSuccessful(true);
        recognitionResult.setItems(new ArrayList<RecognitionItem>());
        recognitionResult.setExceptions(new ArrayList<RecognitionException>());
        // 商品数量累加map
        HashMap<String, RecognitionItem> resMap = new HashMap<>();
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
                RecognitionException recognitionException = handleException(i, beginWeight, endWeight, ExceptionEnum.SENSOR_ERROR);
                recognitionResult.getExceptions().add(recognitionException);
                continue;
            }
            // 放入异物约束
            if (endWeight > beginWeight) {
                RecognitionException recognitionException = handleException(i, beginWeight, endWeight, ExceptionEnum.FOREIGN_OBJECT);
                recognitionResult.getExceptions().add(recognitionException);
                continue;
            }
            // 没有变化
            if (endWeight.equals(beginWeight)) {
                continue;
            }

            // 拿到当前层的商品信息
            List<LayerGoods> layerGoods = new ArrayList<>();
            int finalI = i;
            List<Stock> tmp = stockList.stream().filter(stock -> stock.getLayer() == finalI).toList();
            for (Stock stock : tmp) {
                String goodsId = stock.getGoodsId();
                Integer weight = goodsMap.get(goodsId);
                if (weight != null && weight > 0) {
                    layerGoods.add(new LayerGoods(goodsId, weight, stock.getNum()));
                }
            }

            // 识别算法
            List<List<RecognitionItem>> res = recognizeLayerGoods(layerGoods, beginWeight, endWeight, sensorTolerance);
            // 如果是空或者大于一个组合，则返回异常
            if (res.size() != 1) {
                // 转交其他方式 ...
                RecognitionException recognitionException = handleException(i, beginWeight, endWeight, ExceptionEnum.UNRECOGNIZED);
                recognitionResult.getExceptions().add(recognitionException);
                continue;
            }

            // 对结果进行去重，商品数量累加
            List<RecognitionItem> recognitionItems = res.getFirst();
            for (RecognitionItem item : recognitionItems) {
                if (resMap.containsKey(item.getGoodsId())) {
                    RecognitionItem recognitionItem = resMap.get(item.getGoodsId());
                    recognitionItem.setNum(recognitionItem.getNum() + item.getNum());
                    resMap.put(item.getGoodsId(), recognitionItem);
                    continue;
                }
                resMap.put(item.getGoodsId(), item);
            }
        }
        resMap.forEach((key, value) -> {
            recognitionResult.getItems().add(value);
        });
        return recognitionResult;
    }

    /**
     * 封装异常类返回
     *
     * @param i
     * @param beginWeight
     * @param endWeight
     * @param exceptionEnum
     * @return
     */
    private static RecognitionException handleException(int i, Integer beginWeight, Integer endWeight, ExceptionEnum exceptionEnum) {
        RecognitionException recognitionException = new RecognitionException();
        recognitionException.setLayer(i);
        recognitionException.setBeginWeight(beginWeight == null ? -1 : beginWeight);
        recognitionException.setEndWeight(endWeight == null ? -1 : endWeight);
        recognitionException.setException(exceptionEnum);
        return recognitionException;
    }


    /**
     * 识别当前层用户拿的商品组合
     *
     * @param beginWeight
     * @param endWeight
     * @param layerGoods
     * @param sensorTolerance
     */
    public static List<List<RecognitionItem>> recognizeLayerGoods(List<LayerGoods> layerGoods, Integer beginWeight, Integer endWeight, int sensorTolerance) {
        List<List<RecognitionItem>> dfsResult = new ArrayList<>();
        int diff = (beginWeight - endWeight);
        int minDiff = diff - sensorTolerance;
        int maxDiff = diff + sensorTolerance;
        // 进行深搜
        dfs(layerGoods, dfsResult, new ArrayList<>(), minDiff, maxDiff, 0, 0);

        return dfsResult;
    }

    /**
     * @param layerGoods     当前层商品列表
     * @param res            结果
     * @param curCombination 当前商品组合
     * @param minDiff        拿的重量最小范围
     * @param maxDiff        拿的重量最大范围
     * @param index          当前商品索引
     * @param curWeight      当前重量
     */
    private static void dfs(List<LayerGoods> layerGoods, List<List<RecognitionItem>> res, List<RecognitionItem> curCombination, int minDiff, int maxDiff, int index, int curWeight) {
        if (curWeight >= minDiff && curWeight <= maxDiff) {
            res.add(new ArrayList<>(curCombination));
            return;
        }

        // 重量超出或已遍历所有商品，停止递归
        if (curWeight > maxDiff || index >= layerGoods.size()) {
            return;
        }

        // 当前商品信息
        LayerGoods currentGood = layerGoods.get(index);
        String goodsId = currentGood.getGoodsId();
        int weight = currentGood.getWeight();
        int maxNum = currentGood.getNum();

        // 0 不选当前商品，> 0 选当前商品
        for (int num = 0; num <= maxNum; num++) {
            int newWeight = curWeight + weight * num;
            // 剪枝，后面的没有必要遍历
            if (newWeight > maxDiff) break;

            // 选则添加到当前组合中
            if (num > 0) {
                RecognitionItem item = new RecognitionItem();
                item.setGoodsId(goodsId);
                item.setNum(num);
                curCombination.add(item);
            }

            // 处理下一个
            dfs(layerGoods, res, curCombination, minDiff, maxDiff, index + 1, newWeight);

            // 后序中进行回溯
            if (num > 0) {
                curCombination.removeLast();
            }
        }


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
