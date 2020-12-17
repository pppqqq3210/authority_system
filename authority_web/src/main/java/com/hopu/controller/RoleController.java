package com.hopu.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hopu.domain.Menu;
import com.hopu.domain.Role;
import com.hopu.domain.User;
import com.hopu.domain.UserRole;
import com.hopu.result.PageEntity;
import com.hopu.result.ResponseEntity;
import com.hopu.service.IRoleService;
import com.hopu.service.IUserRoleService;
import com.hopu.utils.UUIDUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.hopu.result.ResponseEntity.*;

@Controller
@RequestMapping("role")
public class RoleController {
    @Autowired
    private IRoleService roleService;

    @Autowired
    private IUserRoleService userRoleService;

    @RequiresPermissions("role:list")
    @RequestMapping("toListPage")
    public String roleList(){
        return "admin/role/role_list";
    }

    @ResponseBody
    @RequestMapping("list")
    public PageEntity roleList(@RequestParam(value = "page",defaultValue = "1") int page, @RequestParam(value = "limit",defaultValue = "1") int limit, Role role, Model model){
        Page<Role> page2 = new Page<>(page, limit);
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>(new Role());
        if (role != null) {
            if (!StringUtils.isEmpty(role.getRole())){
                queryWrapper.like("role",role.getRole());
            }
        }

        IPage<Role> roleIPage = roleService.page(page2, queryWrapper);
        return new PageEntity(roleIPage);
    }

    @RequiresPermissions("role:add")
    @RequestMapping("toAddPage")
    public String toAddPage(){
        return "admin/role/role_add";
    }

    @PostMapping("save")
    @ResponseBody
    public ResponseEntity addRole(Role role){
        Role role2 = roleService.getOne(new QueryWrapper<Role>().eq("role", role.getRole()));
        if (role2 != null) {
            return error("角色名已存在");
        }
        role.setId(UUIDUtils.getID());
        role.setCreateTime(new Date());
        roleService.save(role);
        return success();
    }

    @RequiresPermissions("role:update")
    @RequestMapping("toUpdatePage")
    public String toUpdatePage(String id,Model model){
        Role role = roleService.getById(id);
        model.addAttribute("role",role);
        return "admin/role/role_update";
    }

    @ResponseBody
    @RequestMapping("update")
    public ResponseEntity updateRole(Role role){
        role.setUpdateTime(new Date());
        roleService.updateById(role);
        return success();
    }

    @RequiresPermissions("role:delete")
    @RequestMapping("delete")
    @ResponseBody
    public ResponseEntity delete(@RequestBody ArrayList<Role> roles){
        try {
            List<String> list = new ArrayList<>();
            for (Role role : roles){
                if ("root".equals(role.getRole())){
                    throw new Exception("root角色不能被删除");
                }
                list.add(role.getId());
            }
            roleService.removeByIds(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success();
    }

    @RequiresPermissions("role:setMenu")
    @RequestMapping("toSetMenuPage")
    public String toSetMenuPage(String id,Model model){
        model.addAttribute("role_id",id);
        return "admin/role/role_setMenu";
    }

    @RequestMapping("setMenu")
    @ResponseBody
    public ResponseEntity setMenu(String id, @RequestBody ArrayList<Menu> menus){
        roleService.setMenu(id,menus);
        return success();
    }

    @ResponseBody
    @RequestMapping("roleList")
    public PageEntity List(String userId,Role role){
        List<UserRole> userRoles = userRoleService.list(new QueryWrapper<UserRole>().eq("user_id",userId));
        QueryWrapper<Role> queryWrapper = new QueryWrapper<Role>();
        if (role != null) {
            if (!StringUtils.isEmpty(role.getRole())){
                queryWrapper.like("role",role.getRole());
            }
        }
        List<Role> roles = roleService.list(queryWrapper);
        ArrayList<JSONObject> list = new ArrayList<JSONObject>();
        for (Role role2 : roles) {
            JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(role2));
            boolean rs = false;
            for (UserRole userRole : userRoles) {
                if (userRole.getRoleId().equals(role2.getId())){
                    rs = true;
                }
            }
            jsonObject.put("LAY_CHECKED",rs);
            list.add(jsonObject);
        }
        return new PageEntity(list.size(),list);
    }

}
