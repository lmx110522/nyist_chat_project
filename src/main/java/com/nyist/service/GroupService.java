package com.nyist.service;

import com.nyist.mapper.GroupMapper;
import com.nyist.pojo.Tgroup;
import com.nyist.util.NyistResult;
import org.apache.ibatis.annotations.Insert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class GroupService {

    @Autowired
    private GroupMapper groupMapper;

    public Tgroup findTGroupById(Integer id){
        Tgroup group = groupMapper.findTGroupById(id);
        return group;
    }

    public NyistResult add_group(Integer id, String gname, Integer select_val, Integer oid) {
        if(select_val == null){
            Tgroup group = groupMapper.findGname(gname,id);
            if(group == null){
                group = new Tgroup();
                group.setUid(id).setGname(gname);
                groupMapper.add_group1(group);
            }
            groupMapper.update_group(group.getId(),oid,id);
            groupMapper.update_other_group(oid,id);
            return NyistResult.ok();
        }
        groupMapper.update_other_group(oid,id);
        groupMapper.update_group(select_val,oid,id);
        return NyistResult.ok();
    }
}
