package com.sense.app.core.agent.web;

import com.sense.app.core.agent.domain.CoreAiModel;
import com.sense.app.core.agent.service.CoreAiModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 模型管理接口
 */
@RestController
@RequestMapping("/api/cbi/smart/model")
public class CoreAiModelController {

    @Autowired
    private CoreAiModelService modelService;

    /**
     * 按类型查询模型列表
     *
     * @param modelType 模型类型：Chat、Embedding
     * @return 模型列表
     */
    @GetMapping("/query")
    public ResponseEntity<List<CoreAiModel>> queryByType(@RequestParam(value = "modelType", required = false) String modelType) {
        List<CoreAiModel> models = modelService.queryByType(modelType);
        return ResponseEntity.ok(models);
    }

    @GetMapping("/chat/query")
    public ResponseEntity<List<CoreAiModel>> queryChatModel() {
        List<CoreAiModel> models = modelService.queryChatModel();
        return ResponseEntity.ok(models);
    }

    /**
     * 新建/更新模型
     *
     * @param model 模型信息
     * @return 模型ID
     */
    @PostMapping("/submit")
    public ResponseEntity<Long> submit(@RequestBody CoreAiModel model) {
        Long id = modelService.submit(model);
        return ResponseEntity.ok(id);
    }

    /**
     * 批量删除模型
     *
     * @param ids 模型ID集合
     * @return 是否成功
     */
    @PostMapping("/delete")
    public ResponseEntity<Boolean> delete(@RequestBody List<Long> ids) {
        modelService.deleteBatch(ids);
        return ResponseEntity.ok(true);
    }
}

