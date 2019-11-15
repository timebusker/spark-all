package com.timebusker.phoenix.repository;

import com.timebusker.phoenix.entity.PhoenixUserEntity;
import org.apache.ibatis.annotations.Insert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Description:UserRespostry
 * @Author:Administrator
 * @Date2019/11/13 22:40
 **/
@Repository
public interface PhoenixUserRepository extends JpaRepository<PhoenixUserEntity, String> {

    Page<PhoenixUserEntity> getAllByNameContains(String name, Pageable pageable);

    PhoenixUserEntity getByIdx(String idx);

    @Insert("upsert into tb_phoenix_user (IDX,NAME,SEX,AGE,ADDRESS) VALUES (#{phoenix.idx},#{phoenix.name},#{phoenix.sex},#{phoenix.age},#{phoenix.address})")
    PhoenixUserEntity save(PhoenixUserEntity phoenix);
}
