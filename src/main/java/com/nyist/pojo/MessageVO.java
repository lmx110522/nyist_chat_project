package com.nyist.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class MessageVO {

    private Integer type;

    private Integer userNum;

    private String message;

    private String username;

    private String date;

    private String head_img;

    private Integer from;

    private User fromUser;

    private Integer to;

    private User toUser;
}
