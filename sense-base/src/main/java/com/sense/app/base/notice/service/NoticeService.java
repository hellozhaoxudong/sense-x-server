package com.sense.app.base.notice.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sense.app.base.notice.domain.BaseNotice;
import com.sense.app.base.notice.mapper.NoticeMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class NoticeService extends ServiceImpl<NoticeMapper, BaseNotice> {

    /**
     * 分页查询通知公告列表
     * @param mybatisPage 分页参数
     * @param notice 查询条件
     * @return 通知公告列表
     */
    public List<BaseNotice> queryNoticeList(Page mybatisPage, BaseNotice notice) {
        LambdaQueryWrapper<BaseNotice> queryWrapper = new LambdaQueryWrapper<>();
        
        queryWrapper.select()
                .like(StrUtil.isNotBlank(notice.getNoticeTitle()), BaseNotice::getNoticeTitle, notice.getNoticeTitle())
                .eq(StrUtil.isNotBlank(notice.getNoticeType()), BaseNotice::getNoticeType, notice.getNoticeType())
                .eq(StrUtil.isNotBlank(notice.getNoticeLevel()), BaseNotice::getNoticeLevel, notice.getNoticeLevel())
                .eq(StrUtil.isNotBlank(notice.getNoticeStatus()), BaseNotice::getNoticeStatus, notice.getNoticeStatus())
                .orderByDesc(BaseNotice::getShowTop)
                .orderByDesc(BaseNotice::getCreateDate);
        
        Page<BaseNotice> pageResult = page(mybatisPage, queryWrapper);
        return pageResult.getRecords();
    }
}
