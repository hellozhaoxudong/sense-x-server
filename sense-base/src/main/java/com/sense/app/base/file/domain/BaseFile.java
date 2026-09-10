package com.sense.app.base.file.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sense.app.core.config.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文件管理表
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("base_file")
public class BaseFile extends BaseDomain {

    /**
     * 文件OID
     */
    @TableId
    private String oid;

    /**
     * 业务类型
     */
    @TableField
    private String businessType;

    /**
     * 文件名
     */
    @TableField
    private String fileName;

    /**
     * 文件类型，后缀
     */
    @TableField
    private String fileType;

    /**
     * 存储方式:
     * DB: 数据库存储
     */
    @TableField
    private String storeType;

    @TableField
    private Long tenantId;

}
