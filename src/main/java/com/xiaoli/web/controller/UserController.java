package com.xiaoli.web.controller;

import com.xiaoli.web.entity.User;
import com.xiaoli.web.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {
    private Logger log = Logger.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @RequestMapping("/showAll")
    public String showAll(Model model) {
        log.info("查询所有用户信息");
        List<User> userList = userService.selectAllUser();
        model.addAttribute("userList", userList);
        return "userinfo";
    }

    @RequestMapping("/showUserById")
    public String showUserById(Long id, Model model) {
        User user = userService.selectUserById(id);
        List<User> userList = new ArrayList<User>();
        userList.add(user);
        model.addAttribute("userList", userList);
        return "userinfo";
    }

    @RequestMapping("/showUserByEmail")
    public String showUserByEmail(String email, Model model) {
        User user = userService.selectUserByPhoneOrEmail(email);
        List<User> userList = new ArrayList<User>();
        userList.add(user);
        model.addAttribute("userList", userList);
        return "userinfo";
    }

    @RequestMapping("/toDemo")
    public String toDemoPage() {
        return "demo";
    }

    @RequestMapping("/demo")
    @ResponseBody
    public Map<String, Object> demo(String username, String age, String sex) {
        System.out.println("======================" + username + "\t" + age + "\t" + sex);
        Map<String, Object> map = new HashMap<>();
        map.put("name", username);
        map.put("age", age);
        map.put("sex", sex);
        return map;
    }

}
