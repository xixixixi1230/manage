package com.example.demo.controller;

import com.example.demo.domain.JwtUtil;
import com.example.demo.domain.User;
import com.example.demo.domain.UsersList;
import com.example.demo.service.UserService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
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
    public ResponseEntity<?> findUsersList(){
        List<UsersList> user=userService.findUsersList();
        System.out.println(user.size());
        Map<String, Object> response = new HashMap<>();
        response.put("code",20000);
        response.put("data",user);
        response.put("total",user.size());
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
        System.out.println(user);

        // 生成JWT令牌
//        String token = jwtUtil.generateToken(username);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "登录成功");
        response.put("username", username);
        response.put("code",20000);
        response.put("token",user.getToken());
//        response.put("token", token); // 将生成的令牌添加到响应中

        return ResponseEntity.ok(response);
    }
}

class UserLoginRequest {
    private String username;
    private String password;

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}


@Data
class UserListRequest{
    private String userName;
    private String trueName;
    private String roleIds;
    private String password;
    private String email;
    private Integer gender;
    private String address;
    private String introduction;
    private String phone;
    private String avatarurl;


}
