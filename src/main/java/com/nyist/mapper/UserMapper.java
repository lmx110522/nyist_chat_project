package com.nyist.mapper;

import com.nyist.pojo.Tgroup;
import com.nyist.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {

    @Select("select * from `user` where username=#{username} and password=#{password}")
    public User getUser(User user);

    @Select("select * from `user` where id=#{uid}")
    public User getUserById(Integer uid);

    @Select("select * from tgroup where uid = #{id}")
    List<Tgroup> getMyGroup(Integer id);

    @Update("update user set head_img=#{path} where id=#{id}")
    void changeImage(@Param("path") String path, @Param("id") Integer id);
}
