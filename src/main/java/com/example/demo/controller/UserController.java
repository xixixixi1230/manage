package com.example.demo.controller;

import com.example.demo.domain.User;
import com.example.demo.domain.UsersList;
import com.example.demo.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dev/user")
public class UserController {

    @Autowired
    private UserService userService;

    // 增加用户
    @PostMapping("")
    public ResponseEntity<?> add(@RequestBody UsersList usersList){
        System.out.println(usersList);
        boolean sta= userService.add(usersList);
        Map<String, Object> response = new HashMap<>();
        response.put("code",20000);
        response.put("data",true);
        return ResponseEntity.ok(response);
    }

    // 删除用户
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudent(@RequestParam("id") Integer id){
        System.out.println("delete"+id);
        userService.delete(id);
        Map<String, Object> response = new HashMap<>();
        response.put("code",20000);
        response.put("data","删除成功");
        return ResponseEntity.ok(response);
    }

    // 更改用户
    @PostMapping("/update/{id}")
    public ResponseEntity<?> update(@RequestBody UsersList usersList){
        boolean sta= userService.update(usersList);
        Map<String, Object> response = new HashMap<>();
        response.put("code",20000);
        response.put("data",sta);
        return ResponseEntity.ok(response);
    }

    // 查询用户
    @GetMapping("/users/{id}")
    public UsersList findUsersListById(@RequestParam("id") Integer id){
        return userService.findUsersListById(id);
    }

    // 登入账户查询,RequestParam要有param
    @GetMapping("/account/{name}")
    public User findUserById(@RequestParam("name") String name){
        return userService.findUserById(name);
    }

    // 所有登入账户
    @GetMapping("/list")
    public List<User> findUserList(){
        return userService.findUserList();
    }

    // 所有用户信息
    @GetMapping("/users/list")
    public ResponseEntity<?> findUsersList(@RequestParam(value = "pageNum")Integer pageNum,
                                           @RequestParam(value = "pageSize")Integer pageSize,
                                           @RequestParam("name") String name,
                                           @RequestParam("minCreateTime") String minCreateTime,
                                           @RequestParam("maxCreateTime") String maxCreateTime,
                                            Model model){
        PageHelper.startPage(pageNum,pageSize);
        List<UsersList> users=userService.findUsersList(name,minCreateTime,maxCreateTime);
        // 获取分页信息
        PageInfo<UsersList> userPageInfo = new PageInfo<>(users);

        // 构建响应
        Map<String, Object> response = new HashMap<>();
        response.put("total", userPageInfo.getTotal());
        response.put("code", 20000);
        response.put("data", userPageInfo.getList());

        // 将分页信息添加到模型
        model.addAttribute("userPageInfo", userPageInfo);

        return ResponseEntity.ok(response);
    }

    // 登入账户信息
    @GetMapping("/info")
    public ResponseEntity<?> findInfoByToken(@RequestParam("token") String token){
        User user=userService.findInfoByToken(token);
        Map<String, Object> response = new HashMap<>();
        response.put("code",20000);
        response.put("data",user);
        return ResponseEntity.ok(response);
    }

    // 登录
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        User user=findUserById(username);
        Map<String, Object> response = new HashMap<>();
        if(user.getPassword().equals(password)){
            response.put("message", "登录成功");
            response.put("username", username);
            response.put("code",20000);
            response.put("token",user.getToken());
            return ResponseEntity.ok(response);
        }
        else{
            response.put("message", "用户名或密码不匹配");
            response.put("code",50012);
            return ResponseEntity.ok(response);
        }




    }
}


class UserLoginRequest {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}