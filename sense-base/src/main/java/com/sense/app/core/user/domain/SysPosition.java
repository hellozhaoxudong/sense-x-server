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
 * @ClassName SysPosition
 * @description 岗位信息
 * @author sense-x
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@TableName("core_sys_position")
public class SysPosition extends BaseDomain implements Serializable {

    /**
     * ID
     */
    @TableId
    private Long id;

    /**
     * 岗位编码
     */
    @TableField
    private String postCode;

    /**
     * 岗位名称
     */
    @TableField
    private String postName;

    /**
     * 岗位简称
     */
    @TableField
    private String postShortName;

    /**
     * 岗位职级
     */
    @TableField
    private String postRank;

    /**
     * 岗位序列
     */
    @TableField
    private String postSequence;

    /**
     * 编制人数
     */
    @TableField
    private Integer employeeCount;

    /**
     * 状态 Y/N
     */
    @TableField
    private String status;

    /**
     * 租户ID
     */
    @TableField
    private Long tenantId;
}
