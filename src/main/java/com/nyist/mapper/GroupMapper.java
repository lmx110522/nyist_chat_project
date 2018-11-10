package com.nyist.mapper;

import com.nyist.pojo.Tgroup;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupMapper {

    @Select("select * from tgroup where id=#{id}")
    public Tgroup findTGroupById(Integer id);

    @Insert("insert into tgroup(gname, uid) values (#{gname}, #{uid})")
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
    void add_group1(Tgroup tgroup);

    @Insert("insert into friend(gid, oid,mid,is_pass) values (#{gid}, #{oid}, #{mid},1)")
    void update_group(@Param("gid") Integer id, @Param("oid") Integer oid, @Param("mid") Integer id1);

    @Update("update friend set is_pass=1 where mid=#{mid} and oid=#{oid}")
    void update_other_group(@Param("mid") Integer oid, @Param("oid") Integer id);

    @Select("select * from tgroup where gname=#{gname} and uid=#{uid}")
    Tgroup findGname(@Param("gname") String gname, @Param("uid") Integer id);
}
