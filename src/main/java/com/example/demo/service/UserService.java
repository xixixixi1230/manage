package com.example.demo.service;

import com.example.demo.domain.User;
import com.example.demo.domain.UsersList;

import java.util.List;

public interface UserService {

    User findUserById(String name);

    List<User> findUserList();

    List<UsersList> findUsersList();

    User findInfoByToken(String token);

    boolean add(UsersList usersList);

    boolean update(UsersList usersList);

    int delete(Integer id);

    UsersList findUsersListById(Integer id);
}