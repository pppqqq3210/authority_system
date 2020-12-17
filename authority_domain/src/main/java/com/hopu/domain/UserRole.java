package com.hopu.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
@Data
@TableName("t_user_role")
public class UserRole implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userId;
    private String roleId;
}
