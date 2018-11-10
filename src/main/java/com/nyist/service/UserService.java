package com.nyist.service;

import com.nyist.mapper.UserMapper;
import com.nyist.pojo.FileMessage;
import com.nyist.pojo.Friend;
import com.nyist.pojo.Tgroup;
import com.nyist.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private FriendService friendService;

    public User login(User user){
        String password = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(password);
        return  userMapper.getUser(user);
    }

    public User getUserById(Integer uid){
        return  userMapper.getUserById(uid);
    }

    public Friend updateFriend(Integer mid,Integer oid,String dataTime,String message){
        return  friendService.updateFriend(mid,oid,dataTime,message);
    }

    public List<FileMessage> getChatContent(String filename, Integer counts) throws IOException {
        if(counts == null){
            counts = 1;
        }
        Integer startLine = (counts-1)*10+1;
        Integer endLine = (counts)*10;
        List<String> messages = listFileByRegionRow("C:/nyist/" + filename + ".txt", startLine, endLine);
        List<FileMessage> fileMessages = new ArrayList<>();

        for (String one_message : messages) {
            FileMessage fileMessage = new FileMessage();
            String[] strings = one_message.split("&&&&&");
            fileMessage.setHeadImg(strings[0]);
            fileMessage.setUid(strings[1]);
            fileMessage.setUsername(strings[2]);
            fileMessage.setFDate(strings[3]);
            fileMessage.setMessage(strings[4]);
            fileMessages.add(fileMessage);
        }
        return  fileMessages;
    }
    private   List<String> listFileByRegionRow(String path, Integer startLine, Integer endLine) {
        List<String> strList = getFileContent(path);
        //指定区间的值存到regionList
        List<String> regionList = new ArrayList<String>();
        int size = strList.size();
        if(size >= (endLine - 1)) {
            for (int i=startLine; i<=endLine; i++)
                regionList.add(strList.get(i-1));
        }
        else{
            for (int i=startLine; i<=size; i++)
                regionList.add(strList.get(i-1));
        }

        return regionList;
    }
    public static List<String> getFileContent(String path) {
        List<String> strList = new ArrayList<String>();
        try {
            File file = new File(path);
            InputStreamReader read = new InputStreamReader(new FileInputStream(file), "UTF-8");
            BufferedReader reader = new BufferedReader(read);
            String line;
            while((line = reader.readLine()) != null) {
                strList.add(line);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strList;
    }

    public List<Tgroup> getMyGroup(Integer id) {
       return  userMapper.getMyGroup(id);
    }

    public User changeImage(String path, Integer id) {
        userMapper.changeImage(path,id);
        User user = userMapper.getUserById(id);
        return user;
    }
}
