package com.sense.app.base.company.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sense.app.core.config.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 企业信息
 * @author sense-x
 * @date 2026-01-01 23:00:00
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("base_company")
public class BaseCompany extends BaseDomain {

    /**
     * 企业ID
     */
    @TableId
    private Long id;

    /**
     * 企业名称
     */
    @TableField
    private String companyName;

    /**
     * 企业编码
     */
    @TableField
    private String companyCode;

    /**
     * 统一社会信用代码
     */
    @TableField
    private String socialCreditCode;

    /**
     * 法人
     */
    @TableField
    private String legalPerson;

    /**
     * 联系人姓名
     */
    @TableField
    private String contactName;

    /**
     * 联系人电话
     */
    @TableField
    private String contactPhone;

    /**
     * 省份
     */
    @TableField
    private String province;

    /**
     * 城市
     */
    @TableField
    private String city;

    /**
     * 详细地址
     */
    @TableField
    private String address;

    /**
     * 状态
     */
    @TableField
    private Integer status;

}
