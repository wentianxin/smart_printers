package com.qg.smpt.web.repository;

import com.qg.smpt.web.model.User;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);
    
    List<User> selectAllUser();
    
    User selectByLogin(User user);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    int updateLogo(User user);

    User selectUserPrinter(Integer id);
}