package com.wenxin.ai.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Date;
import java.time.LocalDateTime;

@Data
@TableName("orders")
public class Order {
    private String orderId;
    private int userId;
    private double money;
    private String details;
    private int status;
    private LocalDateTime createTime;
    private LocalDateTime finishTime;
}