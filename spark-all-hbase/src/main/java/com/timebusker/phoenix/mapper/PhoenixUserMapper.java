package com.timebusker.phoenix.mapper;

import com.timebusker.phoenix.entity.PhoenixUserEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @Description:PhoenixUserMapper
 * @Author:Administrator
 * @Date2019/11/14 23:20
 **/
@Mapper
public interface PhoenixUserMapper {

    @Insert("upsert into tb_phoenix_user (IDX,NAME,SEX,AGE,ADDRESS) VALUES (#{phoenix.idx},#{phoenix.name},#{phoenix.sex},#{phoenix.age},#{phoenix.address})")
    public void save(@Param("phoenix") PhoenixUserEntity phoenix);

    @Delete("delete from tb_phoenix_user WHERE idx=#{idx}")
    public void delete(@Param("idx") int idx);

    @Select("select * from tb_phoenix_user WHERE idx=#{idx}")
    public PhoenixUserEntity getById(@Param("idx") String idx);

    @Select("select * from tb_phoenix_user WHERE NAME=#{name}")
    public List<PhoenixUserEntity> getByName(@Param("name") String name);

    @Select("select * from tb_phoenix_user")
    public List<PhoenixUserEntity> getAll();
}
