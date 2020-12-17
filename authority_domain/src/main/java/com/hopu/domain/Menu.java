package com.hopu.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@TableName("t_menu")
@Data
public class Menu extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private String pid;
    private String menuName;
    private Integer menuType;
    private String menuImg;
    private String permiss;
    private String url;
    private String functionImg;
    private String seq;

    @TableField(exist = false)
    private List<Menu> nodes;

}
