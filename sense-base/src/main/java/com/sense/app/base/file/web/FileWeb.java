package com.sense.app.base.file.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sense.app.base.file.domain.BaseFile;
import com.sense.app.base.file.service.FileService;
import com.sense.app.core.utils.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/base/file")
public class FileWeb {

    @Autowired
    private FileService fileService;

    /**
     * 上传文件
     * @param file 文件
     * @param businessType 业务类型
     * @return 文件信息
     */
    @PostMapping("/upload")
    public ResponseEntity<BaseFile> uploadFile(@RequestParam("file") MultipartFile file,
                                               @RequestParam("businessType") String businessType,
                                               @RequestParam(value="storeType", defaultValue = "DB") String storeType) throws IOException {
        BaseFile baseFile = fileService.uploadFile(file, businessType, storeType);
        return new ResponseEntity<>(baseFile, HttpStatus.OK);
    }

    /**
     * 左侧：查询文件业务类型列表及对应文件数量（不分页）
     * @return 业务类型列表，每项包含 businessType 和 count
     */
    @GetMapping("/businessTypes")
    public ResponseEntity<List<Map<String, Object>>> queryBusinessTypes() {
        List<Map<String, Object>> types = fileService.queryBusinessTypes();
        return new ResponseEntity<>(types, HttpStatus.OK);
    }

    /**
     * 右侧：根据业务类型分页查询文件列表
     * @param businessType 业务类型
     * @param page 当前页
     * @param pageSize 每页条数
     * @return 文件列表
     */
    @GetMapping("/page")
    public ResponseEntity<List<BaseFile>> queryFilesByBusinessType(@RequestParam(value = "businessType", required = false) String businessType,
                                                                   @RequestParam(value = "page", defaultValue = "1") int page,
                                                                   @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        Page mybatisPage = PageUtil.getPage(page, pageSize);
        List<BaseFile> files = fileService.queryFilesByBusinessType(mybatisPage, businessType);
        return new ResponseEntity(files, PageUtil.getTotalHeader(mybatisPage), HttpStatus.OK);
    }

    /**
     * 批量删除文件
     * @param oids 文件OID列表
     * @return 删除结果
     */
    @PostMapping("/delete")
    public ResponseEntity deleteFiles(@RequestBody List<String> oids) {
        fileService.deleteFiles(oids);
        return ResponseEntity.ok(true);
    }
}
