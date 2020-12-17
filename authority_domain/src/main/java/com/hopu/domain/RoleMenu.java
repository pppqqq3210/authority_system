package com.hopu.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("t_role_menu")
public class RoleMenu implements Serializable {
    private static final long serialVersionUID = 1L;

    private String menuId;
    private String roleId;

}
