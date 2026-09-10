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
 * 文件存储表
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("base_file_store")
public class BaseFileStore extends BaseDomain {

    /**
     * 文件OID
     */
    @TableId
    private String oid;

    /**
     * 文件内容
     */
    @TableField
    private String fileContent;

    @TableField
    private Long tenantId;
}
