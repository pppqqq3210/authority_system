package com.hopu.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("t_role")
public class Role extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private String role;
    private String remark;

}
