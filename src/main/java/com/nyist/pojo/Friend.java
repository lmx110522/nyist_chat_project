package com.nyist.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class Friend {

    private Integer id;

    private Integer mid;

    private User mainUser;

    private Integer oid;

    private User otherUser;

    private String chatDir;

    private String tGroup;

    private Integer gid;

    private Tgroup group;

    private String lastDate;

    private String lastText;

    private Integer isRead; // 0已读，1未读

    private Integer isPass; //是否同意为好友,0还没有验证同意,1已同意
}
