package com.sense.app.base.file.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sense.app.base.file.domain.BaseFile;
import com.sense.app.base.file.domain.BaseFileStore;
import com.sense.app.base.file.mapper.BaseFileMapper;
import com.sense.app.base.file.mapper.BaseFileStoreMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@Transactional(rollbackFor = Exception.class)
public class FileService extends ServiceImpl<BaseFileMapper, BaseFile> {

    @Autowired
    private BaseFileStoreMapper fileStoreMapper;

    /**
     * 上传文件
     * 文件内容以Base64编码存储到数据库
     * @param file 上传的文件
     * @param businessType 业务类型
     * @return 文件信息
     */
    @Transactional(rollbackFor = Exception.class)
    public BaseFile uploadFile(MultipartFile file, String businessType, String storeType) throws IOException {
        // 获取文件原始名
        String fileName = file.getOriginalFilename();

        String fileType = "txt";
        if (StrUtil.isNotBlank(fileName) && fileName.contains(".")) {
            fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
        }

        // 唯一OID
        String oid = IdWorker.get32UUID() + "." + fileType;

        // 保存文件元数据
        BaseFile baseFile = BaseFile.builder()
                .oid(oid)
                .businessType(businessType)
                .fileName(fileName)
                .fileType(fileType)
                .storeType(storeType)
                .build();
        save(baseFile);

        // 保存文件内容
        String base64Content = Base64.getEncoder().encodeToString(file.getBytes());
        BaseFileStore fileStore = BaseFileStore.builder()
                .oid(oid)
                .fileContent(base64Content)
                .build();
        fileStoreMapper.insert(fileStore);

        return baseFile;
    }

    /**
     * 查询所有业务类型及对应文件数量（不分页）
     * @return 业务类型列表，每项包含 businessType 和 count
     */
    public List<Map<String, Object>> queryBusinessTypes() {
        // 先查出所有去重的业务类型
        LambdaQueryWrapper<BaseFile> typeWrapper = new LambdaQueryWrapper<>();
        typeWrapper.select(BaseFile::getBusinessType)
                .groupBy(BaseFile::getBusinessType);
        List<String> types = listObjs(typeWrapper, obj -> (String) obj);

        // 遍历每个类型，逐个count
        List<Map<String, Object>> result = new ArrayList<>();
        for (String type : types) {
            LambdaQueryWrapper<BaseFile> countWrapper = new LambdaQueryWrapper<>();
            countWrapper.eq(BaseFile::getBusinessType, type);
            long count = count(countWrapper);
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("businessType", type);
            item.put("count", count);
            result.add(item);
        }
        return result;
    }

    /**
     * 根据业务类型分页查询文件列表
     * @param mybatisPage 分页参数
     * @param businessType 业务类型
     * @return 文件列表
     */
    public List<BaseFile> queryFilesByBusinessType(Page mybatisPage, String businessType) {
        LambdaQueryWrapper<BaseFile> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StrUtil.isNotBlank(businessType), BaseFile::getBusinessType, businessType)
                .orderByDesc(BaseFile::getBusinessType);
        Page<BaseFile> pageResult = page(mybatisPage, queryWrapper);
        return pageResult.getRecords();
    }

    /**
     * 批量删除文件（同时删除文件元数据和存储内容）
     * @param oids 文件OID列表
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteFiles(List<String> oids) {
        if (oids == null || oids.isEmpty()) return;
        // 删除文件元数据
        removeByIds(oids);
        // 删除文件存储内容
        LambdaQueryWrapper<BaseFileStore> storeWrapper = new LambdaQueryWrapper<>();
        storeWrapper.in(BaseFileStore::getOid, oids);
        fileStoreMapper.delete(storeWrapper);
    }
}
