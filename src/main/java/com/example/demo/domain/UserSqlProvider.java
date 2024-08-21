package com.example.demo.domain;

import java.util.Map;

public class UserSqlProvider {
    public String findFilteredUsers(Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM userslist WHERE 1=1");

        if (params.get("name") != null && !params.get("name").toString().isEmpty()) {
            sql.append(" AND userName = #{name}");
        }

        if (params.get("minCreateTime") != null && !params.get("minCreateTime").toString().isEmpty()) {
            sql.append(" AND createTime >= #{minCreateTime}");
        }

        if (params.get("maxCreateTime") != null && !params.get("maxCreateTime").toString().isEmpty()) {
            sql.append(" AND createTime <= #{maxCreateTime}");
        }

        return sql.toString();
    }
}
