package com.haonan.demo.enums;

public enum ExceptionEnum {
    SENSOR_ERROR("传感器异常", 50010, "SENSOR_EXCEPTION"),
    FOREIGN_OBJECT("异物异常", 50020, "FOREIGN_OBJECT_EXCEPTION"), // 异物异常
    UNRECOGNIZED("无法识别异常", 50030, "UNRECOGNIZED_EXCEPTION"); // 无法识别异常
    /**
     * 描述
     */
    private String text;
    /**
     * 异常码
     */
    private Integer code;
    /**
     * 实际存储值
     */
    private String value;

    ExceptionEnum(String text, Integer code, String value) {
        this.text = text;
        this.code = code;
        this.value = value;
    }


    // 需补充getter方法以获取成员变量值
    public String getText() {
        return text;
    }

    public Integer getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }
}
