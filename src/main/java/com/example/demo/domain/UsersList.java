package com.example.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsersList {
    @Id//代表主键
    @GeneratedValue(strategy = GenerationType.IDENTITY)//主键自增策略
    private Integer id;
    private String userName;
    private String trueName;
    private String roleIds;
    private Integer status;
    private String password;
    private String email;
    private Integer gender;
    private String address;
    private String introduction;
    private String phone;
    private String avatarurl;
    private Date createTime;
}
