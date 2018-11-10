package com.nyist.controller;

import com.google.gson.Gson;
import com.nyist.pojo.*;
import com.nyist.service.FriendService;
import com.nyist.service.GroupService;
import com.nyist.service.UserService;
import com.nyist.util.IDUtils;
import com.nyist.util.NyistResult;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.misc.BASE64Decoder;
import sun.security.util.KeyUtil;

import javax.servlet.http.HttpSession;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private FriendService friendService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private ConstantQiniu constantQiniu;

    @RequestMapping(value = {"/","/loginUI"})
    public String loginUI(){
        return "loginUI";
    }

    @RequestMapping("/chat")
    public String chat(HttpSession session){
        User user = (User) session.getAttribute("user");
        if(user != null){
            List<CustomFriend> customFriends;
//            customFriends = (List<CustomFriend>) session.getAttribute("customFriends");
//            if(customFriends == null){
//
//            }
            customFriends = friendService.findFriendList(user);
            session.setAttribute("customFriends",customFriends);
            return "main";
        }
        else{
            return "redirect:/loginUI";
        }

    }

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public String login(User user, Model model, HttpSession session){
         user = userService.login(user);
         if(user != null){
             session.setAttribute("user",user);
             return "redirect:/chat";
         }
        model.addAttribute("error","用户或密码不正确");
        return "loginUI";
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:loginUI";
    }

    @ResponseBody
    @RequestMapping("/getChatRecord")
    public List<Friend> getChatRecord(String uid,HttpSession session){
        int mid = Integer.parseInt(uid);
        List<Friend> friends = friendService.getChatRecord(mid,session);
        return friends;
    }

    @ResponseBody
    @RequestMapping("/getUserByName")
    public NyistResult getUserByName(String username){
        User user = friendService.getUserByName(username);
        if(user == null){
            return  NyistResult.build(500,"没有此用户，请重新查找！");
        }
        return NyistResult.ok(user);
    }

    @ResponseBody
    @RequestMapping("/getChatContent")
    public List<FileMessage> getChatContent(Integer oid,Integer uid,Integer counts) throws IOException {
        String filename = oid > uid?(oid+"&"+uid):(uid+"&"+oid);
        List<FileMessage> fileMessages =  userService.getChatContent(filename,counts);
        return fileMessages;
    }

    @ResponseBody
    @RequestMapping("/setHasRead")
    public String setHasRead(Integer uid,Integer oid){
        friendService.setHasRead(uid,oid);
        return "ok";
    }


    @ResponseBody
    @RequestMapping("/addFriend")
    public NyistResult addFriend(Integer oid,HttpSession session){
        User user = (User) session.getAttribute("user");
        NyistResult result = friendService.addFriend(user.getId(),oid);
        return result;
    }
    @ResponseBody
    @RequestMapping("/wait_agree")
    public List<User> wait_agree(HttpSession session){
        User user = (User) session.getAttribute("user");
        List<User> userList = friendService.wait_agree(user);
        return userList;
    }

    @ResponseBody
    @RequestMapping("/getMyGroup")
    public List<Tgroup> getMyGroup(HttpSession session){
        User user = (User) session.getAttribute("user");
        List<Tgroup> tgroupList = userService.getMyGroup(user.getId());
        return  tgroupList;
    }
    @ResponseBody
    @RequestMapping(value = "/add_group",method = RequestMethod.POST)
    public NyistResult add_group(Integer select_val,String gname,Integer oid,HttpSession session){
        User user = (User) session.getAttribute("user");
        NyistResult result = groupService.add_group(user.getId(),gname,select_val,oid);
        return result;
    }
    @ResponseBody
    @RequestMapping("/refuseAdd")
    public NyistResult refuseAdd(Integer oid,HttpSession session){
        User user = (User) session.getAttribute("user");
        friendService.refuseAdd(user.getId(),oid);
        return NyistResult.ok();
    }
    @ResponseBody
    @RequestMapping(value = "/changeHeadImg",method = RequestMethod.POST)
    public NyistResult changeHeadImg(String imgSrc,HttpSession session) throws IOException {
        Integer pos = imgSrc.indexOf("64,");
        imgSrc = imgSrc.substring(pos+3);
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] b = decoder.decodeBuffer(imgSrc);
        for(int i=0;i<b.length;++i)
        {
            if(b[i]<0)
            {
                //调整异常数据
                b[i]+=256;
            }
        }
        //生成jpeg图片
        String path = uploadQNImg(new ByteArrayInputStream(b), IDUtils.genImageName()); // KeyUtil.genUniqueKey()生成图片的随机名
        path = "http://"+path;
        User user = (User) session.getAttribute("user");
        session.removeAttribute("user");
        User user1 = userService.changeImage(path, user.getId());
        session.setAttribute("user",user1);
        return NyistResult.ok(path);
    }
    private String uploadQNImg(InputStream file, String key) {
        // 构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone2());
        // 其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        // 生成上传凭证，然后准备上传

        try {
            Auth auth = Auth.create(constantQiniu.getAccessKey(), constantQiniu.getSecretKey());
            String upToken = auth.uploadToken(constantQiniu.getBucket());
            try {
                Response response = uploadManager.put(file, key, upToken,null,null);
                // 解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);

                String returnPath = constantQiniu.getPath() + "/" + putRet.key;
                return returnPath;
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
