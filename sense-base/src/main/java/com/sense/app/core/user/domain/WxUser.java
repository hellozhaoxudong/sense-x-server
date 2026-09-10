package com.sense.app.core.user.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sense.app.core.config.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 微信注册用户
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@TableName("core_sys_wx_user")
public class WxUser extends BaseDomain implements Serializable {

    /**
     * 用户ID
     */
    @TableId
    private Long id;

    /**
     * 微信ID
     */
    @TableField
    private String wxId;

    /**
     * 用户姓名
     */
    @TableField
    private String name;

    /**
     * 电话
     */
    @TableField
    private String phone;

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

}