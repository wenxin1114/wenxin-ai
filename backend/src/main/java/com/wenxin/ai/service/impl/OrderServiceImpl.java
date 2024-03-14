package com.wenxin.ai.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wenxin.ai.entity.Order;
import com.wenxin.ai.mapper.OrderMapper;
import com.wenxin.ai.service.OrderService;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
}
