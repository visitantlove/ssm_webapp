package com.xiaoli.web.service.impl;

import com.xiaoli.web.dao.IUserDao;
import com.xiaoli.web.entity.User;
import com.xiaoli.web.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private IUserDao userDao;

    public User selectUserById(Long userId) {
        return userDao.selectUserById(userId);
    }

    public User selectUserByPhoneOrEmail(String emailOrPhone) {
        return userDao.selectUserByPhoneOrEmail(emailOrPhone);
    }

    public List<User> selectAllUser() {
        return userDao.selectAllUser();
    }
}
