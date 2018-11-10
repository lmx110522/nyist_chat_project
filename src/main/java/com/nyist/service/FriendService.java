package com.nyist.service;

import com.nyist.mapper.FriendMapper;
import com.nyist.mapper.GroupMapper;
import com.nyist.pojo.CustomFriend;
import com.nyist.pojo.Friend;
import com.nyist.pojo.Tgroup;
import com.nyist.pojo.User;
import com.nyist.util.IDUtils;
import com.nyist.util.NyistResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class FriendService {
    
    @Autowired
    private FriendMapper friendMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private GroupMapper groupMapper;

    public List<CustomFriend> findFriendList(User user) {
        List<Friend> friendList = friendMapper.findFriendList(user);
        List<Integer> gids = friendMapper.findGidsGroup(user);
        List<CustomFriend> customFriends = new ArrayList<>();
        if(friendList != null){
            for (Integer gid : gids) {
                CustomFriend customFriend = new CustomFriend();
                List<User> userList = new ArrayList<>();
                for (Friend friend : friendList) {
                    if(friend.getGid() == gid){
                        Integer oid = friend.getOid();
                        User otherUser = userService.getUserById(oid);
                        userList.add(otherUser);
                    }
                }
                Tgroup group = groupService.findTGroupById(gid);
                customFriend.setTgroup(group);
                customFriend.setUserList(userList);
                customFriends.add(customFriend);
            }
            return customFriends;
        }
        return null;
    }

    public Friend updateFriend(Integer mid, Integer oid,String dataTime,String message) {
        return  friendMapper.updateFriend(mid,oid,dataTime,message);
    }

    public List<Friend> getChatRecord(int mid, HttpSession session) {
        List<Friend> friendList = friendMapper.getChatRecord(mid);
        List<Friend> newFriendList = new ArrayList<>();
        List<Integer> stringList = new ArrayList<>();
        User user1 = (User) session.getAttribute("user");
        for (Friend friend : friendList) {
            Integer oid = friend.getOid();
            if(user1.getId() == oid){
                oid = friend.getMid();
            }
            if(!stringList.contains(oid)){
                stringList.add(oid);
                User user = userService.getUserById(oid);
                friend.setOtherUser(user);
                newFriendList.add(friend);
            }

        }
        return newFriendList;
    }

    public User getUserByName(String username) {
       return friendMapper.getUserByName(username);
    }

    public void setHasRead(Integer uid, Integer oid) {
        friendMapper.setHasRead(uid,oid);
    }

    public NyistResult addFriend(Integer id, Integer oid) {
        Friend friend = friendMapper.findIsExist(id,oid);
        if(friend != null){
            if(friend.getIsPass() == 1){
                return  NyistResult.build(500,"对方已经是你好友了,刷新页面查看！");
            }
            else{
                return  NyistResult.build(500,"对方还没有同意，请耐心等待~");
            }
        }
        Friend friend1 = friendMapper.findIsExist(oid,id);
        if(friend1 != null){
            return  NyistResult.build(500,"对方已经添加你了，你还没有同意~");
        }
        List<Tgroup> myGroup = userService.getMyGroup(id);
        Integer gid=null;
        Integer flag = 0;
        if(myGroup != null && myGroup.size() > 0){
            for (Tgroup tgroup : myGroup) {
                if(tgroup.getGname().equals("新朋友")){
                    gid = tgroup.getId();
                    flag++;
                }
            }
        }
        if(myGroup == null || myGroup.size() == 0 || flag == 0){
            Tgroup tgroup = new Tgroup();
            tgroup.setGname("新朋友").setUid(id);
            groupMapper.add_group1(tgroup);
            gid = tgroup.getId();
        }
        friendMapper.addFriend(id,oid,gid);
        return NyistResult.build(200,"好友申请发送成功，请耐心等待对方同意！");
    }

    public List<User> wait_agree(User user) {
       List<Friend> friendList = friendMapper.findIsNotPass(user.getId());
       List<User> userList = new ArrayList<>();
        for (Friend friend : friendList) {
            User mainUser = userService.getUserById(friend.getMid());
            userList.add(mainUser);
        }
        return userList;
    }

    public void refuseAdd(Integer id, Integer oid) {
        friendMapper.refuseAdd(id,oid);
    }
}
