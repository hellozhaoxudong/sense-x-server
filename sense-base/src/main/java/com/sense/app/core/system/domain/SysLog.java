package com.sense.app.core.system.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sense.app.core.config.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author shenfulong
 * @date 2026-01-01 23:00:00
 * @description 用户日志实体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("core_sys_log")
public class SysLog extends BaseDomain {

    /**
     * ID
     */
    @TableId
    private Long id;

    /**
     * 操作用户ID
     */
    @TableField
    private Long userId;

    /**
     * 操作用户姓名
     */
    @TableField
    private String userName;

    /**
     *  操作 IP 地址
     */
    @TableField
    private String userIp;

    /**
     * 操作时间
     */
    @TableField
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date logDate;

    /**
     * 请求类型，如 GET / POST / PUT
     */
    @TableField
    private String reqMethod;

    /**
     * 请求 URL
     */
    @TableField
    private String reqUrl;

    /**
     * 接口状态
     */
    @TableField
    private Integer repStatus;

    /**
     * 请求耗时
     */
    @TableField
    private Long repCost;

    /**
     * 日志内容（描述）
     */
    @TableField
    private String logMsg;

    /**
     * 租户ID
     */
    @TableField
    private Long tenantId;

}
