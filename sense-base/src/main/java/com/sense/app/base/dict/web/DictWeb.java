package com.sense.app.base.dict.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sense.app.base.dict.domain.BaseDict;
import com.sense.app.base.dict.service.DictService;
import com.sense.app.core.utils.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/base/dict")
public class DictWeb {

    @Autowired
    private DictService service;

    /**
     * 分页查询字典列表
     * @author sense-x
     * @date 2026-01-01 23:00:00
     * @param dictName 字典名称
     * @param dictCode 字典编码
     * @param page 当前页
     * @param pageSize 每页条数
     */
    @GetMapping("/page")
    public ResponseEntity<List<BaseDict>> queryDictList(@RequestParam(value = "dictName", required = false) String dictName,
                                                         @RequestParam(value = "dictCode", required = false) String dictCode,
                                                         @RequestParam(value = "page", defaultValue = "1") int page,
                                                         @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        Page mybatisPage = PageUtil.getPage(page, pageSize);
        
        BaseDict query = new BaseDict();
        query.setDictName(dictName);
        query.setDictCode(dictCode);
        
        List<BaseDict> dicts = service.queryDictList(mybatisPage, query);
        return new ResponseEntity(dicts, PageUtil.getTotalHeader(mybatisPage), HttpStatus.OK);
    }

    /**
     * 提交字典
     * @param data 字典信息
     * @return 字典ID
     */
    @PostMapping("/submit")
    public ResponseEntity<Long> submitDict(@RequestBody BaseDict data) {
        service.saveOrUpdate(data);
        return new ResponseEntity<>(data.getId(), HttpStatus.OK);
    }

    /**
     * 删除字典
     * @param ids 字典ID列表
     * @return 删除结果
     */
    @PostMapping("/delete")
    public ResponseEntity deleteDict(@RequestBody List<Long> ids) {
        service.removeByIds(ids);
        return ResponseEntity.ok(true);
    }
}
