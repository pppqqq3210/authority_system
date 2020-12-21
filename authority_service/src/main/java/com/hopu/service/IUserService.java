package com.hopu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hopu.domain.Role;
import com.hopu.domain.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

public interface IUserService extends IService<User> {
    void setRole(String id, List<Role> roles);


    void addPic(String imgName, MultipartFile file);

    void updatePic(String userImg, String imgName, MultipartFile file);

    void deletePics(List<String> userImgs);
}
