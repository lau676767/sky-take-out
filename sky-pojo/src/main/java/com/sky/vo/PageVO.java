package com.sky.vo;

import lombok.Data;

import java.util.List;

@Data
public class PageVO<T> {
    private long total;
    private List<T> records;

    public PageVO(long total, List<T> records) {
        this.total = total;
        this.records = records;
    }
}
