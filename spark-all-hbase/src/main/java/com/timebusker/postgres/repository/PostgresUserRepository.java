package com.timebusker.postgres.repository;

import com.timebusker.postgres.entity.PostgresUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Description:UserRespostry
 * @Author:Administrator
 * @Date2019/11/13 22:40
 **/
@Repository
public interface PostgresUserRepository extends JpaRepository<PostgresUserEntity, String> {

}
