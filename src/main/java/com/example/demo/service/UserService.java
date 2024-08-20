package com.example.demo.service;

import com.example.demo.domain.User;
import com.example.demo.domain.UsersList;

import java.util.Date;
import java.util.List;

public interface UserService {

    User findUserById(String name);

    List<User> findUserList();

    List<UsersList> findUsersList();

    User findInfoByToken(String token);

    boolean add(UsersList usersList);

    boolean update(UsersList usersList);

    int delete(Integer id);

//    int update(Integer id, String userName, String trueName, String roleIds, String password, String email, Integer gender, String address, String introduction, String phone, String avatarurl, Date createTime);

    UsersList findUsersListById(Integer id);
}