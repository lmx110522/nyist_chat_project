package com.nyist.pojo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CustomFriend {

    private Tgroup tgroup;

    private List<User> userList;

}
