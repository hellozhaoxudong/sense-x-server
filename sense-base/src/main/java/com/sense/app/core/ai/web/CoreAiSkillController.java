package com.sense.app.core.ai.web;

import com.sense.app.core.ai.domain.CoreAiSkill;
import com.sense.app.core.ai.service.CoreAiSkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * AI技能管理接口
 */
@RestController
@RequestMapping("/api/core/ai/skill")
public class CoreAiSkillController {

    @Autowired
    private CoreAiSkillService skillService;

    /**
     * 查询技能列表
     */
    @GetMapping("/query")
    public ResponseEntity<List<CoreAiSkill>> query(
            @RequestParam(value = "skillName", required = false) String skillName,
            @RequestParam(value = "skillTag", required = false) String skillTag) {
        List<CoreAiSkill> list = skillService.queryList(skillName, skillTag);
        return ResponseEntity.ok(list);
    }

    /**
     * 新建/更新技能
     */
    @PostMapping("/submit")
    public ResponseEntity<Long> submit(@RequestBody CoreAiSkill skill) {
        Long id = skillService.submit(skill);
        return ResponseEntity.ok(id);
    }

    /**
     * 批量删除技能
     */
    @PostMapping("/delete")
    public ResponseEntity<Boolean> delete(@RequestBody List<Long> ids) {
        skillService.deleteBatch(ids);
        return ResponseEntity.ok(true);
    }
}
