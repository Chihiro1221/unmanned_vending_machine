package com.haonan.demo.pojo;

import java.util.List;

public class RecognitionResult {
    // 没有任何异常时，为 true
    private boolean successful;
    // 允许 empty 不允许 null；成功且为 empty 时即为无购物
    private List<RecognitionItem> items;
    // 允许 empty 不允许 null
    private List<RecognitionException> exceptions;

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public List<RecognitionItem> getItems() {
        return items;
    }

    public void setItems(List<RecognitionItem> items) {
        this.items = items;
    }

    public List<RecognitionException> getExceptions() {
        return exceptions;
    }

    public void setExceptions(List<RecognitionException> exceptions) {
        this.exceptions = exceptions;
    }

    @Override
    public String toString() {
        return "RecognitionResult{" +
                "successful=" + successful +
                ", items=" + items +
                ", exceptions=" + exceptions +
                '}';
    }
}