package com.nyist.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class Tgroup{

    private Integer id;

    private String gname;

    private Integer uid;

    private User user;
}
