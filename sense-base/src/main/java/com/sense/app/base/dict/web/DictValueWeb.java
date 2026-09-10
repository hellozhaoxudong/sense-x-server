package com.sense.app.base.dict.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sense.app.base.dict.domain.BaseDictValue;
import com.sense.app.base.dict.service.DictValueService;
import com.sense.app.core.utils.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/base/dict/value")
public class DictValueWeb {

    @Autowired
    private DictValueService service;

    /**
     * 分页查询字典值列表
     * @author sense-x
     * @date 2026-01-01 23:00:00
     * @param dictCode 字典编码
     * @param valueName 字典值名
     * @param status 状态
     * @param page 当前页
     * @param pageSize 每页条数
     */
    @GetMapping("/page")
    public ResponseEntity<List<BaseDictValue>> queryDictValueList(@RequestParam(value = "dictCode", required = false) String dictCode,
                                                                   @RequestParam(value = "valueName", required = false) String valueName,
                                                                   @RequestParam(value = "status", required = false) String status,
                                                                   @RequestParam(value = "page", defaultValue = "1") int page,
                                                                   @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        Page mybatisPage = PageUtil.getPage(page, pageSize);
        
        BaseDictValue query = new BaseDictValue();
        query.setDictCode(dictCode);
        query.setValueName(valueName);
        query.setStatus(status);
        
        List<BaseDictValue> dictValues = service.queryDictValueList(mybatisPage, query);
        return new ResponseEntity(dictValues, PageUtil.getTotalHeader(mybatisPage), HttpStatus.OK);
    }

    /**
     * 提交字典值
     * @param data 字典值信息
     * @return 字典值ID
     */
    @PostMapping("/submit")
    public ResponseEntity<Long> submitDictValue(@RequestBody BaseDictValue data) {
        service.saveOrUpdate(data);
        return new ResponseEntity<>(data.getId(), HttpStatus.OK);
    }

    /**
     * 删除字典值
     * @param ids 字典值ID列表
     * @return 删除结果
     */
    @PostMapping("/delete")
    public ResponseEntity deleteDictValue(@RequestBody List<Long> ids) {
        service.removeByIds(ids);
        return ResponseEntity.ok(true);
    }
}
