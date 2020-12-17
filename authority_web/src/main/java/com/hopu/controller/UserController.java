package com.hopu.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hopu.domain.Role;
import com.hopu.domain.User;
import com.hopu.service.IUserService;
import com.hopu.utils.ShiroUtils;
import com.hopu.utils.UUIDUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import com.hopu.result.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.hopu.result.ResponseEntity.*;

@Controller
@RequestMapping("user")
public class UserController {
    @Autowired
    private IUserService userService;

//    @RequestMapping("list")
//    @ResponseBody
//    public ResponseEntity<List<User>> list(){
//        List<User> list = userService.list();
//        return new ResponseEntity<List<User>>(list, HttpStatus.FOUND);
//    }


    @RequiresPermissions("user:list")
    @RequestMapping("toListPage")
    public String userList(){
        return "admin/user/user_list";
    }

    @RequestMapping("list")
    @ResponseBody
    public IPage<User> userList(@RequestParam(value = "page",defaultValue = "1") int page,@RequestParam(value = "limit",defaultValue = "1") int limit, User user, Model model){
        Page<User> userPage = new Page<>(page, limit);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>(new User());
        if (user != null) {
            if (!StringUtils.isEmpty(user.getUserName())){
                queryWrapper.like("user_name",user.getUserName());
            }
            if (!StringUtils.isEmpty(user.getTel())){
                queryWrapper.like("tel",user.getTel());
            }
            if (!StringUtils.isEmpty(user.getEmail())){
                queryWrapper.like("email",user.getEmail());
            }
        }
        IPage<User> userIPage = userService.page(userPage,queryWrapper);
        return userIPage;
    }

    @RequiresPermissions("user:add")
    @RequestMapping("toAddPage")
    public String toAddPage(){
        return "admin/user/user_add";
    }

    @RequestMapping("save")
    @ResponseBody
    public ResponseEntity addUser(User user){
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.eq("user_name",user.getUserName());
        User one = userService.getOne(queryWrapper);
        if (one != null) {
            return error("用户名已存在");
        }
        user.setId(UUIDUtils.getID());
        user.setSalt(UUIDUtils.getID());
        ShiroUtils.encPass(user);
        user.setCreateTime(new Date());
        userService.save(user);
        return success();
    }

    @RequiresPermissions("user:update")
    @RequestMapping("toUpdatePage")
    public String toUpdatePage(String id,Model model){
        User user = userService.getById(id);
        model.addAttribute("user",user);
        return "admin/user/user_update";
    }

    @ResponseBody
    @RequestMapping("update")
    public ResponseEntity updateUser(User user){
        ShiroUtils.encPass(user);
        user.setUpdateTime(new Date());
        userService.updateById(user);
        return success();
    }

    @RequiresPermissions("user:delete")
    @ResponseBody
    @RequestMapping("delete")
    public ResponseEntity delete(@RequestBody ArrayList<User> users){
        try {
            List<String> list = new ArrayList<>();
            for (User user : users){
                if ("root".equals(user.getUserName())){
                    throw new Exception("root账号不能被删除");
                }
                list.add(user.getId());
            }
            userService.removeByIds(list);
        } catch (Exception e) {
            e.printStackTrace();
            return error(e.getMessage());
        }
        return success();
    }

    @RequiresPermissions("user:setRole")
    @RequestMapping("toSetRole")
    public String toSetRole(String id,Model model){
        model.addAttribute("user_id",id);
        return "admin/user/user_setRole";
    }

    @ResponseBody
    @RequestMapping("setRole")
    public ResponseEntity setRole(String id, @RequestBody ArrayList<Role> roles){
        userService.setRole(id,roles);
        return success();
    }
}
