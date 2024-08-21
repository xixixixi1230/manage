package com.example.demo.service.impl;

import com.example.demo.dao.UserMapper;
import com.example.demo.domain.User;
import com.example.demo.domain.UsersList;
import com.example.demo.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServicelmpl implements UserService {
    @Autowired
    private UserMapper UserMapper;

    // 添加用户
    @Override
    public boolean add(UsersList usersList) {
        return UserMapper.add(usersList);
    }

    // 删除
    @Override
    public int delete(Integer id) {
        return UserMapper.delete(id);
    }

    public boolean update(UsersList usersList) {
        return UserMapper.update(usersList);
    }

    // 查
    @Override
    public UsersList findUsersListById(Integer id) {
        return UserMapper.findUsersListById(id);
    }

    @Override
    public User findUserById(String name) {
        return UserMapper.findUserById(name);
    }

    @Override
    public List<User> findUserList() {
        return UserMapper.findUserList();
    }

    @Override
    public List<UsersList> findUsersList(String name,String minCreateTime,String maxCreateTime) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("minCreateTime", minCreateTime);
        params.put("maxCreateTime", maxCreateTime);

        return UserMapper.findUsersList(params);
    }

    @Override
    public User findInfoByToken(String token){
        return UserMapper.findInfoByToken(token);
    }

}
