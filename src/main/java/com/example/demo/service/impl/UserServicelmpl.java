package com.example.demo.service.impl;

import com.example.demo.dao.UserMapper;
import com.example.demo.domain.User;
import com.example.demo.domain.UsersList;
import com.example.demo.service.UserService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserServicelmpl implements UserService {
    @Autowired
    private UserMapper UserMapper;

    // 添加用户
    @Override
    public boolean add(UsersList usersList) {
        return UserMapper.add(usersList);
    }
//
//    /**
//     * 更新 User
//     * @param name
//     * @param age
//     * @param id
//     * @return
//     */
//    @Override
//    public int update(String name, Integer age, Integer id) {
//        return UserMapper.update(name,age,id);
//    }

    // 删除
    @Override
    public int delete(Integer id) {
        return UserMapper.delete(id);
    }

    // 更新
//    @Override
//    public int update(Integer id, String userName, String trueName, String roleIds, String password, String email, Integer gender, String address, String introduction, String phone, String avatarurl, Date createTime) {
//        return UserMapper.update(id, userName, trueName, roleIds, password, email, gender, address, introduction, phone, avatarurl,createTime);
//    }
    public boolean update(UsersList usersList) {
        return UserMapper.update(usersList);
    }

    // 查
    @Override
    public UsersList findUsersListById(Integer id) {
        return UserMapper.findUsersListById(id);
    }

    /**
     * 根据 id 查询 User
     * @param name
     * @return
     */
    @Override
    public User findUserById(String name) {
        return UserMapper.findUserById(name);
    }

    /**
     * 查询所有的 User
     * @return
     */
    @Override
    public List<User> findUserList() {
        return UserMapper.findUserList();
    }

    @Override
    public User findInfoByToken(String token){
        return UserMapper.findInfoByToken(token);
    }
}
