package com.hopu.service.impl;

import com.hopu.utils.OSSUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hopu.domain.Role;
import com.hopu.domain.User;
import com.hopu.domain.UserRole;
import com.hopu.mapper.UserMapper;
import com.hopu.service.IUserRoleService;
import com.hopu.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Autowired
    private IUserRoleService userRoleService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setRole(String id, List<Role> roles) {
        userRoleService.remove(new QueryWrapper<>(new UserRole()).eq("user_id",id));
        for (Role role : roles) {
            UserRole userRole = new UserRole();
            userRole.setUserId(id);
            userRole.setRoleId(role.getId());
            userRoleService.save(userRole);
        }
    }

    @Override
    public void addPic(String imgName,MultipartFile userImg) {
        try {
            OSSUtils.uploadPic(imgName,userImg.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updatePic(String userImg, String imgName, MultipartFile file) {
        try {
            OSSUtils.uploadPic(imgName,file.getInputStream());
            OSSUtils.deletePic(userImg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deletePics(List<String> userImgs) {
        try {
            userImgs.forEach(OSSUtils::deletePic);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
