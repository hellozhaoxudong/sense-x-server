package com.sense.app.core.system.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sense.app.core.config.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 系统配置
 * @author sense-x
 * @date 2026-01-01 23:00:00
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("core_sys_config")
public class CoreSysConfig extends BaseDomain {

    /**
     * ID
     */
    @TableId
    private Long id;

    /**
     * 配置KEY
     */
    @TableField
    private String configKey;

    /**
     * 配置值
     */
    @TableField
    private String configValue;

    /**
     * 租户ID
     */
    @TableField
    private Long tenantId;

}
