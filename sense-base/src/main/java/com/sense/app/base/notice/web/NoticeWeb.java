package com.sense.app.base.notice.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sense.app.base.notice.domain.BaseNotice;
import com.sense.app.base.notice.service.NoticeService;
import com.sense.app.core.utils.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/base/notice")
public class NoticeWeb {

    @Autowired
    private NoticeService service;

    /**
     * 分页查询通知公告列表
     * @author sense-x
     * @date 2026-01-01 23:00:00
     * @param noticeTitle 标题
     * @param noticeType 类型
     * @param noticeLevel 级别
     * @param noticeStatus 状态
     * @param page 当前页
     * @param pageSize 每页条数
     */
    @GetMapping("/page")
    public ResponseEntity<List<BaseNotice>> queryNoticeList(@RequestParam(value = "noticeTitle", required = false) String noticeTitle,
                                                         @RequestParam(value = "noticeType", required = false) String noticeType,
                                                         @RequestParam(value = "noticeLevel", required = false) String noticeLevel,
                                                         @RequestParam(value = "noticeStatus", required = false) String noticeStatus,
                                                         @RequestParam(value = "page", defaultValue = "1") int page,
                                                         @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        Page mybatisPage = PageUtil.getPage(page, pageSize);
        
        BaseNotice query = new BaseNotice();
        query.setNoticeTitle(noticeTitle);
        query.setNoticeType(noticeType);
        query.setNoticeLevel(noticeLevel);
        query.setNoticeStatus(noticeStatus);
        
        List<BaseNotice> notices = service.queryNoticeList(mybatisPage, query);
        return new ResponseEntity(notices, PageUtil.getTotalHeader(mybatisPage), HttpStatus.OK);
    }

    /**
     * 提交通知公告
     * @param data 通知公告信息
     * @return 通知公告ID
     */
    @PostMapping("/submit")
    public ResponseEntity<Long> submitNotice(@RequestBody BaseNotice data) {
        service.saveOrUpdate(data);
        return new ResponseEntity<>(data.getId(), HttpStatus.OK);
    }

    /**
     * 删除通知公告
     * @param ids 通知公告ID列表
     * @return 删除结果
     */
    @PostMapping("/delete")
    public ResponseEntity deleteNotice(@RequestBody List<Long> ids) {
        service.removeByIds(ids);
        return ResponseEntity.ok(true);
    }
}
