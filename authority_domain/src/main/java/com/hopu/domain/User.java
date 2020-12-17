package com.hopu.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("t_user")
public class User extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private String userName;
    private String password;
    private String salt;
    private String nickname;
    private String userImg;
    private String tel;
    private Integer sex;
    private String email;
    private String status;
}
