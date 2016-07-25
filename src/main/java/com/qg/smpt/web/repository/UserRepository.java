package com.qg.smpt.web.repository;

import com.qg.smpt.web.model.User;
import org.springframework.stereotype.Repository;

/**
 * Created by tisong on 7/23/16.
 */
@Repository
public interface UserRepository {

    User selectUser(int id);

    int  insertUser(User user);
}
