package com.sense.app.core.user.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class CustomRole implements Serializable {

    /**
     * ID
     */
    private Long id;

    /**
     * 编码
     */
    private String roleCode;

    /**
     * 名称
     */
    private String roleName;
}