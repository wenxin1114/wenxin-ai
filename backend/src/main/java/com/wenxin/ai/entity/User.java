package com.wenxin.ai.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.lang.reflect.Type;
import java.sql.Date;
import java.time.LocalDateTime;

@Data
@TableName("users")
public class User {
    @TableId(type = IdType.AUTO)
    @TableField("user_id")
    private Long userId;
    private String username;
    private String password;
    private String email;
    private String salt;
    private Integer status;
    private Integer token;
    private LocalDateTime createTime;
}
