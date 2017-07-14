package com.xiaoli.web.dao;


import com.xiaoli.web.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IUserDao {

    User selectUserById(Long userId);

    User selectUserByPhoneOrEmail(String emailOrPhone);

    List<User> selectAllUser();
}
