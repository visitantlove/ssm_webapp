package com.xiaoli.web.service;

import com.xiaoli.web.entity.User;

import java.util.List;

public interface UserService {

    User selectUserById(Long userId);

    User selectUserByPhoneOrEmail(String emailOrPhone);

    List<User> selectAllUser();
}
