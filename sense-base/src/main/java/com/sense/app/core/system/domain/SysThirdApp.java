package com.sense.app.core.system.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sense.app.core.config.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("core_sys_third_app")
public class SysThirdApp extends BaseDomain {

    /**
     * ID
     */
    @TableId
    private Long id;

    /**
     * 应用标题
     */
    @TableField
    private String appTitle;

    /**
     * 应用版本
     */
    @TableField
    private String appVersion;

    /**
     * 应用描述
     */
    @TableField
    private String appDesc;

    /**
     * 应用图片
     */
    @TableField
    private String appImage;

    /**
     * 应用地址
     */
    @TableField
    private String appUrl;

}
