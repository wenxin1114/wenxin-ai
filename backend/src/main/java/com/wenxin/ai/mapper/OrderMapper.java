package com.wenxin.ai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wenxin.ai.entity.Order;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {
}
