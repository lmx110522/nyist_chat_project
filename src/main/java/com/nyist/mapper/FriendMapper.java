package com.nyist.mapper;

import com.nyist.pojo.Friend;
import com.nyist.pojo.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendMapper {

    @Select("select * from friend where mid=#{id} and is_pass=1")
    List<Friend> findFriendList(User user);

    @Select("select gid from friend where mid=#{id} and is_pass=1  group by gid ")
    List<Integer> findGidsGroup(User user);

    @Select("update friend set last_date=#{dataTime},last_text=#{message},is_read=1 where mid=#{mid} and oid=#{oid} and is_pass=1")
    Friend updateFriend(@Param("mid") Integer mid, @Param("oid") Integer oid,  @Param("dataTime") String dataTime, @Param("message")String message);

    @Select("select * from friend where (mid=#{mid} or oid = #{mid}) and last_text is not null and is_pass=1  group by last_date desc")
    List<Friend> getChatRecord(int mid);

    @Select("select * from `user` where username=#{username}")
    User getUserByName(String username);

    @Update("update friend set is_read = 0 where mid=#{uid} and oid=#{oid} and is_pass=1 ")
    void setHasRead(@Param("uid") Integer uid,@Param("oid") Integer oid);

    @Select("select * from friend where mid=#{mid} and oid=#{oid}")
    Friend findIsExist(@Param("mid") Integer id, @Param("oid") Integer oid);



    @Insert("insert into friend(mid,oid,is_pass,gid) values(#{id},#{oid},0,#{gid})")
    void addFriend( @Param("id")Integer id, @Param("oid")Integer oid,@Param("gid") Integer gid);

    @Select("select * from friend where is_pass = 0 and oid=#{id}")
    List<Friend> findIsNotPass(Integer id);

    @Delete("delete from friend where mid=#{mid} and oid=#{oid}")
    void refuseAdd(@Param("oid") Integer id, @Param("mid") Integer oid);
}
