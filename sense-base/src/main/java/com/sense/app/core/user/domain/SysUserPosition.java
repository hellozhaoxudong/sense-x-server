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
 * @ClassName SysUserPosition
 * @description 用户岗位分配信息
 * @author sense-x
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@TableName("core_sys_user_position")
public class SysUserPosition extends BaseDomain implements Serializable {

    /**
     * ID
     */
    @TableId
    private Long id;

    /**
     * 岗位ID
     */
    @TableField
    private Long positionId;

    /**
     * 用户ID
     */
    @TableField
    private Long userId;

    /**
     * 租户ID
     */
    @TableField
    private Long tenantId;
}
