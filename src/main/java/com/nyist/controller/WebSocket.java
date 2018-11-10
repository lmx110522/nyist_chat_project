package com.nyist.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nyist.pojo.Friend;
import com.nyist.pojo.MessageVO;
import com.nyist.pojo.User;
import com.nyist.service.UserService;
import com.nyist.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpSession;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArraySet;


@Controller
@ServerEndpoint("/webSocket")
@Slf4j
public class WebSocket {

    private Session session;

    private static CopyOnWriteArraySet<WebSocket> webSockets = new CopyOnWriteArraySet<>();

    private MessageVO messageVO = new MessageVO();



    private static ApplicationContext applicationContext;
    private UserService userService;
    public static void setApplicationContext(ApplicationContext applicationContext) {
        WebSocket.applicationContext = applicationContext;
    }
    @OnOpen
    public void onOpen(Session session) {
        userService = applicationContext.getBean(UserService.class);
        this.session = session;
        webSockets.add(this);
        messageVO.setType(1);
        messageVO.setUserNum(webSockets.size());
        messageVO.setMessage("有新的连接");

        ObjectMapper mapper = new ObjectMapper();

        String Json = "";
        try {
            Json = mapper.writeValueAsString(messageVO);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }

        this.sendMessage(Json);
        log.info("【websocket消息】有新的连接, 总数:{}", webSockets.size());
    }


    @OnClose
    public void onClose() {
        webSockets.remove(this);

        messageVO.setType(2);
        messageVO.setUserNum(webSockets.size());
        messageVO.setMessage("有用户离开");

        ObjectMapper mapper = new ObjectMapper();

        String Json = "";
        try {
            Json = mapper.writeValueAsString(messageVO);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }

        this.sendMessage(Json);


        log.info("【websocket消息】连接断开, 总数:{}", webSockets.size());
    }

    @OnMessage
    public void onMessage(String message) throws IOException {
        MessageVO messageVO = JsonUtils.jsonToPojo(message, MessageVO.class);
        if(messageVO.getFrom() != null && messageVO.getTo() != null ){
            User from = userService.getUserById(messageVO.getFrom());
            User to = userService.getUserById(messageVO.getTo());

            String filename = from.getId() > to.getId()?(from.getId()+"&"+to.getId()):(to.getId()+"&"+from.getId());
            String directory = "c:/nyist";
            File file = new File(directory,filename+".txt");
            if(!file.exists()){
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            if(from != null && to != null){
                this.messageVO.setUsername(from.getUsername());
                this.messageVO.setType(3);
                this.messageVO.setFrom(messageVO.getFrom());
                this.messageVO.setFromUser(userService.getUserById(messageVO.getFrom()));
                this.messageVO.setTo(messageVO.getTo());
                this.messageVO.setToUser(userService.getUserById(messageVO.getTo()));
                Date currentTime = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dateString = formatter.format(currentTime);
                this.messageVO.setDate(dateString);
                this.messageVO.setUserNum(webSockets.size());
                this.messageVO.setMessage(message);
                this.messageVO.setHead_img(from.getHeadImg());
                String save_message = generateChatRecord(this.messageVO, dateString,messageVO.getMessage());
                insertNewLine(directory+"/"+filename+".txt",save_message,0);
                Friend friend = userService.updateFriend(messageVO.getFrom(), messageVO.getTo(),dateString,messageVO.getMessage());
                ObjectMapper mapper = new ObjectMapper();
                String Json = "";
                try {
                    Json = mapper.writeValueAsString(this.messageVO);
                } catch (Exception ex) {
                    log.error(ex.getMessage());
                }

                this.sendMessage(Json);
                log.info("【websocket消息】收到客户端发来的消息:{}", message);
            }
        }


    }
    private String generateChatRecord(MessageVO messageVO,  String dateString,String message){
        User user = userService.getUserById(messageVO.getFrom());
        String saveMessage = messageVO.getHead_img()+"&&&&&"+user.getId()+"&&&&&"+user.getUsername()+"&&&&&"+dateString+"&&&&&"+message+"\n";
        return saveMessage;
    }
    private void insertNewLine(String my_file,String insertContent, int line) throws IOException {
        File tmp = File.createTempFile("tmp", null);
        FileOutputStream tmpOut = new FileOutputStream(tmp);
        FileInputStream tmpIn = new FileInputStream(tmp);
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(my_file, "rw");
            byte[] buf = new byte[64];
            int hasRead = 0;
            while ((hasRead = raf.read(buf)) > 0) {
                // 把原有内容读入临时文件
                tmpOut.write(buf, 0, hasRead);
            }
            raf.seek(0L);
            raf.write(insertContent.getBytes());
            // 追加临时文件内容
            while ((hasRead = tmpIn.read(buf)) > 0) {
                raf.write(buf, 0, hasRead);
            }
        } catch (IOException e) {
            System.out.println("写入失败！");
            e.printStackTrace();
        } finally {
            try {
                raf.close();
            } catch (IOException e) {
                System.out.println("写入失败，关闭流失败！");
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String message) {
        for (WebSocket webSocket : webSockets) {
            log.info("【websocket消息】广播消息, message={}", message);
            try {
                webSocket.session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}