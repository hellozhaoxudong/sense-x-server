package com.sense.app.core.user.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sense.app.core.user.domain.SysOrganize;
import com.sense.app.core.user.mapper.SysOrganizeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class SysOrganizeService extends ServiceImpl<SysOrganizeMapper, SysOrganize> {

    @Autowired
    private SysOrganizeMapper apiMapper;

    /**
     * 查询部门树
     * @author sense-x
     * @date 2026-01-01 23:00:00
     */
    public List<SysOrganize> queryOrganizeTree(){
        List<SysOrganize> menus = list(new LambdaQueryWrapper<SysOrganize>().orderByAsc(SysOrganize::getSortNum));

        List<SysOrganize> trees = new ArrayList<>();
        for(SysOrganize menu : menus){
            if (menu.getParentId() == 0){
                trees.add(buildTree(menu, menus));
            }
        }

        return trees;
    }

    /**
     * 查询菜单列表
     * @return 列表结构
     */
    public List<SysOrganize> queryOrganizeList(){
        return list();
    }



    private SysOrganize buildTree(SysOrganize parent, List<SysOrganize> all) {
        List<SysOrganize> children = new ArrayList<>();

        for (SysOrganize node : all) {
            if (parent.getId().equals(node.getParentId())) {
                children.add(buildTree(node, all));
            }
        }

        parent.setChildren(children);
        return parent;
    }


    /**
     * 添加/修改组织
     */
    public Long submitOrganize(SysOrganize data) {
        saveOrUpdate(data);
        return data.getId();
    }

    /**
     * 删除组织
     */
    public void deleteById(List<Long> ids) {
        // TODO: 删除其他信息

        // TODO: 删除子节点


        removeByIds(ids);
    }
}
