package com.sense.app.core.user.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sense.app.core.config.BaseDomain;
import com.sense.app.core.system.domain.CoreSysApp;
import com.sense.app.core.system.domain.CoreSysMenu;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName SysUser
 * @description 用户信息
 * @author sense-x
 * @date 2026-01-01 23:00:00
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@TableName("core_sys_user")
public class SysUser extends BaseDomain implements Serializable {



    /**
     * 用户ID
     */
    @TableId
    private Long id;

    /**
     * 登录名
     */
    @TableField
    private String username;

    /**
     * 密码
     */
    @TableField
    private String password;

    /**
     * 用户姓名
     */
    @TableField
    private String name;

    /**
     * 性别：W/M
     */
    @TableField
    private String sex;

    /**
     * 电话
     */
    @TableField
    private String phone;

    /**
     * 邮箱
     */
    @TableField
    private String email;

    /**
     * 锁定：Y: 锁定/ N: 未锁定
     */
    @TableField
    private String locked;

    /**
     * 是否启用: Y:启用/ N:禁用
     */
    @TableField
    private String enabled;

    /**
     * 语言编码：zh-CN：中文简体 / zh-TW：中文繁体 / en：英语
     */
    @TableField
    private String local;

    /**
     * 角色ID集合
     */
    @TableField(exist = false)
    private List<Long> roleIds;

    /**
     * 用户所有角色名称
     */
    @TableField(exist = false)
    private String roleNames;

    /**
     * 用户所有租户名称
     */
    @TableField(exist = false)
    private String tenantNames;

    /**
     * 全量用户信息补充：租户列表
     */
    @TableField(exist = false)
    private List<SysTenant> tenantList;

    /**
     * 全量用户信息补充：租户
     */
    @TableField(exist = false)
    private SysTenant tenant;

    /**
     * 全量用户信息补充：租户ID
     */
    @TableField(exist = false)
    private Long tenantId;

    /**
     * 全量用户信息补充：角色列表
     */
    @TableField(exist = false)
    private List<SysRole> roleList;

    /**
     * 全量用户信息补充：角色
     */
    @TableField(exist = false)
    private SysRole role;

    /**
     * 全量用户信息补充：应用列表
     */
    @TableField(exist = false)
    private List<CoreSysApp> appList;

}