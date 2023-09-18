package com.like4u.aim.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.like4u.aim.mapper.UserMapper;
import com.like4u.aim.pojo.User;
import com.like4u.aim.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * @author Zhang Min
 * @version 1.0
 * @date 2023/7/15 18:56
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;



    @Override
    public String preCheck(String username, String password) {

        if (username.length()<4|username.length()>20){
            return "用户名长度应在4-20";
        }
        if (password.length()<6||password.length()>20){
            return "密码长度应在6-12之间";
        }
        return null;

    }
}
