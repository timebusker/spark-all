package com.timebusker.mysql.repository;

import com.timebusker.mysql.entity.MySQLUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Description:UserRespostry
 * @Author:Administrator
 * @Date2019/11/13 22:40
 **/
@Repository
public interface MySQLUserRepository extends JpaRepository<MySQLUserEntity, String> {

}
