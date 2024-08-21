package com.example.demo.dao;

import com.example.demo.domain.User;
import com.example.demo.domain.UserSqlProvider;
import com.example.demo.domain.UsersList;
import org.apache.ibatis.annotations.*;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {

    // 增
    @Insert("insert into userslist(userName,trueName,roleIds,password,email,gender,address,introduction,phone,createTime) values(#{userName}, #{trueName}, #{roleIds}, #{password}, #{email}, #{gender}, #{address}, #{introduction}, #{phone},#{createTime})")
    boolean add(UsersList usersList);

    // 删
    @Delete("delete from userslist where id = #{id}")
    int delete(int id);

    // 改
    @Update("update userslist set userName = #{userName}, trueName = #{trueName}, roleIds = #{roleIds}, password = #{password}, email = #{email}, gender = #{gender}, address = #{address}, introduction = #{introduction}, phone = #{phone}, createTime=#{createTime} where id = #{id}")
    boolean update(UsersList usersList);

    // 查
    @Select("select id, userName as userName, trueName as trueName,roleIds as roleIds,password as password,email as email,gender as gender,address as address,introduction as introduction,phone as phone,createTime as createTime from userslist where id = #{id}")
    UsersList findUsersListById(@Param("id") Integer id);

    @Select("select name, password as password, roles as roles,token as token from users where name = #{name}")
    User findUserById(@Param("name") String name);

    @Select("select token, name as name,password as password, roles as roles  from users where token = #{token}")
    User findInfoByToken(@Param("token") String token);

    @Select("select name, password as password, roles as roles,token as token from users")
    List<User> findUserList();

    @SelectProvider(type = UserSqlProvider.class, method = "findFilteredUsers")
    List<UsersList> findUsersList(Map<String, Object> params);


}

