# sql2o-plus (BETA)
[![Build Status](https://travis-ci.org/cotide/sql2o-plus.svg?branch=master)](https://travis-ci.org/cotide/sql2o-plus)
[![License](https://img.shields.io/badge/license-Apache2-blue.svg)](https://github.com/cotide/sql2o-plus/blob/master/LICENSE)
[![Gitter](https://img.shields.io/gitter/room/nwjs/nw.js.svg)](https://gitter.im/sql2o-plus/Lobby)

基于[sql2o](https://github.com/aaberg/sql2o)的轻量级ORM

> Java版本 1.8
 
##  Maven Build

```xshell
clean cobertura:cobertura compile install
```

## 特点

- 集成SQL Linq语法糖
- 集成SQL分页
- CRUD 封装/简化调用方法


## 实体映射 


```java

package com.sqltest.model;

import org.dapper.basic.domain.base.BaseEntityByType;
import org.dapper.core.attr.Column;
import org.dapper.core.attr.Ignore;
import org.dapper.core.attr.PrimaryKey;
import org.dapper.core.attr.Table;

import java.util.Date;

/**
 * 用户信息
 */
@lombok.Getter
@lombok.Setter
@Table("user_info")
public class UserInfo extends BaseEntityByType {

    @PrimaryKey("user_id")
    private int id;

    @Column("user_Name")
    private String name;

    @Column("password")
    private String pwd;

    private int login;

    @Column("creat_time")
    private Date creatTime;

    @Ignore
    private String other;
}

```

### 注解描述

- @Table (表名)
- @PrimaryKey (主键)
- @Column (字段名，如果跟数据库字段名称一致可不用标记该字段)
- @Ignore (忽略字段)


## Dto实体

```java
package com.sqltest.dto;

import lombok.Data;

@Data
public class UserInfoDto {

    private int id;

    private String name;

    private int login;
}
```

## 初始化


```java 
protected Database getDatabase() {
        String url = "jdbc:mysql://192.168.1.100:3307/g_main?useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT%2B8&useSSL=false";
        String user ="test";
        String pass ="123456";
        return new Database(url,user,pass);
} 
```

## 查询

### 列表查询

```java
Database db = getDatabase();
IRepository<UserInfo> userInfoRepository =  db.getRepository(UserInfo.class);

// getList() 
List<UserInfo> result =  userInfoRepository.getList();

// getList(Sql sql) 
List<UserInfo> result2 = userInfoRepository.getList(Sql.builder().select().from(UserInfo.class));

// getList(String sql,Object ... param) 
String sql = "select * from user_info where user_id = @0 ";
List<UserInfo> result3 = userInfoRepository.getList(sql,1);

// Dto getList
// getDtoList(Class<TDto> returnType,Sql sql)
Sql sql1 = Sql.builder().select(" user_id as id, user_Name as name ").from(UserInfo.class).where("user_id = @0",1);
List<UserInfoDto> result = userInfoRepository.getDtoList(UserInfoDto.class,sql1);
  
// getDtoList(Class<TDto> returnType,String sql,Object ... param)
String sql2 = "select user_id as id, user_Name as name from user_info where user_id = @0 ";
List<UserInfoDto> result2 = userInfoRepository.getDtoList(UserInfoDto.class,sql2,1)；
```

 

### 获取对象

```java
Database db = getDatabase();
IRepository<UserInfo> userInfoRepository =  db.getRepository(UserInfo.class);

// getById(Object primaryKey)
UserInfo result =  userInfoRepository.getById(1);

// get(Sql sql)
UserInfo result2 = userInfoRepository.get(
Sql.builder().select().from(UserInfo.class).where("user_id  = @0",1));

// get(String sql, Object ...  param)
String sql = "select * from user_info where user_id = @0 ";
UserInfo result3 = userInfoRepository.get(sql,1);

// Dto get
// getDto(Class<TDto> returnType, Sql sql)
UserInfoDto result1 = userInfoRepository.getDto(
UserInfoDto.class,Sql.builder().select("user_id as id, user_Name as name").from(UserInfo.class).where("user_id  = @0",1));
```

### 分页

```java
Database db = getDatabase();
IRepository<UserInfo> userInfoRepository =  db.getRepository(UserInfo.class);
// getPageList(int pageIndex, int pageSize, Sql sql)
PageList<UserInfo> result = userInfoRepository.getPageList(1,10,Sql.builder().select().from(UserInfo.class));
```


### 新增

```java
Database db = getDatabase();
IRepository<UserInfo> userInfoRepository =  db.getRepository(UserInfo.class);
UserInfo domain = new UserInfo();
domain.setName("Test");
domain.setLogin(10086);
domain.setPwd("123456");
domain.setCreatTime(new Date());
UserInfo user =   userInfoRepository.create(domain);
```

### 修改

```java
Database db = getDatabase();
IRepository<UserInfo> userInfoRepository =  db.getRepository(UserInfo.class);
// get 
UserInfo user =  userInfoRepository.get(Sql.builder().select().from(UserInfo.class).where("user_id = @0",3399));
// update
user.setName("Test_2 ## -- ");
userInfoRepository.update(user); 
```

### 删除

```java
Database db = getDatabase();
IRepository<UserInfo> userInfoRepository =  db.getRepository(UserInfo.class);
// get
UserInfo user =  userInfoRepository.get(Sql.builder().select().from(UserInfo.class).where("user_id = @0",3391));
// delete
userInfoRepository.delete(user);
```