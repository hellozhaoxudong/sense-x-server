package com.sense.app.base.notice.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sense.app.core.config.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


/**
 * 通知公告
 * @author sense-x
 * @date 2026-01-01 23:00:00
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("base_notice")
public class BaseNotice extends BaseDomain {

    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 标题
     */
    @TableField
    private String noticeTitle;

    /**
     * 内容（富文本）
     */
    @TableField
    private String noticeContent;

    /**
     * 摘要（可选）
     */
    @TableField
    private String noticeSummary;

    /**
     * 类型（通知/公告/系统消息等）
     */
    @TableField
    private String noticeType;

    /**
     * 级别（普通/重要/紧急）
     */
    @TableField
    private String noticeLevel;

    /**
     * 状态（草稿/已发布/已下线）
     */
    @TableField
    private String noticeStatus;

    /**
     * 是否置顶Y/N
     */
    @TableField
    private String showTop;

    /**
     * 是否弹窗Y/N
     */
    @TableField
    private String showModal;

    /**
     * 发布时间
     */
    @TableField
    private Date publishDate;

    /**
     * 过期时间
     */
    @TableField
    private Date expireDate;

}
