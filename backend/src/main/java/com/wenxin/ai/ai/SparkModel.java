package com.wenxin.ai.ai;


import com.wenxin.ai.common.dto.ResponseResult;
import com.wenxin.ai.common.holder.RequestHolder;
import com.wenxin.ai.config.model.SparkConfig;
import com.wenxin.ai.dto.ChatDto;
import com.wenxin.ai.entity.ChatMessage;
import io.github.briqt.spark4j.SparkClient;
import io.github.briqt.spark4j.constant.SparkApiVersion;
import io.github.briqt.spark4j.model.SparkMessage;
import io.github.briqt.spark4j.model.SparkSyncChatResponse;
import io.github.briqt.spark4j.model.request.SparkRequest;
import io.github.briqt.spark4j.model.response.SparkTextUsage;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import okhttp3.WebSocketListener;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Component
public class SparkModel extends WebSocketListener {

    @Resource
    public SparkConfig sparkConfig;

    @Resource
    private RedisTemplate<String, SparkMessage> redisTemplate;

    public ResponseResult sendMessage(ChatDto dto) {
        SparkClient sparkClient = new SparkClient();
        Long userId = RequestHolder.getId();
        // 设置认证信息
        sparkClient.appid = sparkConfig.getAppid();
        sparkClient.apiKey = sparkConfig.getApiKey();
        sparkClient.apiSecret = sparkConfig.getApiSecret();
        // 消息列表，可以在此列表添加历史对话记录
        // 从 redis 里获取历史记录
        ListOperations<String, SparkMessage> opsForList = redisTemplate.opsForList();
        opsForList.rightPush("user:chat:" + userId + ":" + dto.getId(), SparkMessage.userContent(dto.getQuestion()));
        List<SparkMessage> messages = opsForList.range("user:chat:" + userId + ":" + dto.getId(), 0, -1);
        // 构造请求
        SparkRequest sparkRequest = SparkRequest.builder()
        // 消息列表
                .messages(messages)
        // 模型回答的tokens的最大长度,非必传，默认为2048。
        // V1.5取值为[1,4096]
        // V2.0取值为[1,8192]
        // V3.0取值为[1,8192]
                .maxTokens(2048)
        // 核采样阈值。用于决定结果随机性,取值越高随机性越强即相同的问题得到的不同答案的可能性越高 非必传,取值为[0,1],默认为0.5
                .temperature(0.2)
        // 指定请求版本，默认使用最新3.0版本
                .apiVersion(SparkApiVersion.V1_5)
                .build();
        // 同步调用
        SparkSyncChatResponse chatResponse = sparkClient.chatSync(sparkRequest);
        SparkTextUsage textUsage = chatResponse.getTextUsage();
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("reply", chatResponse.getContent());
        resultMap.put("totalTokens", textUsage.getTotalTokens());
        // 将聊天记录保存到redis
        opsForList.rightPush("chat:" + dto.getId(), SparkMessage.assistantContent(chatResponse.getContent()));
        return ResponseResult.okResult(resultMap);
    }
}

