package com.sense.app.base.rule.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.net.URLEncodeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sense.app.base.rule.domain.BaseRuleDataDetail;
import com.sense.app.base.rule.mapper.RuleDataDetailMapper;
import com.sense.app.core.exception.BizException;
import com.sense.app.core.utils.SecurityUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 规则数据详情服务
 * @author sense-x
 * @date 2026-01-01 23:00:00
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RuleDataDetailService extends ServiceImpl<RuleDataDetailMapper, BaseRuleDataDetail> {

    /**
     * 分页查询规则数据详情（返回时解密数据内容用于展示）
     * @param mybatisPage 分页参数
     * @param ruleDataId 规则数据ID
     * @return 数据详情列表
     */
    public List<BaseRuleDataDetail> queryDetailPage(Page mybatisPage, Long ruleDataId) {
        Page<BaseRuleDataDetail> pageResult = page(mybatisPage, new LambdaQueryWrapper<BaseRuleDataDetail>()
                .eq(BaseRuleDataDetail::getRuleDataId, ruleDataId)
                .orderByDesc(BaseRuleDataDetail::getCreateDate));
        List<BaseRuleDataDetail> records = pageResult.getRecords();

        // 解密数据内容，仅用于展示
        records.forEach(item -> item.setDataContent(decryptSafely(item.getEncryptData())));

        return records;
    }

    /**
     * 导入数据详情：文件内容需为JSON数组，每个元素为一行数据（JSON对象）
     * @param ruleDataId 规则数据ID
     * @param file 上传文件
     */
    @Transactional(rollbackFor = Exception.class)
    public void importDetail(Long ruleDataId, MultipartFile file) {
        if (ruleDataId == null) {
            throw new BizException("规则数据ID不能为空");
        }
        if (file == null || file.isEmpty()) {
            throw new BizException("请上传文件");
        }

        String content;
        try {
            content = new String(file.getBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new BizException("文件读取失败");
        }

        JSONArray jsonArray;
        try {
            jsonArray = JSONUtil.parseArray(content);
        } catch (Exception e) {
            throw new BizException("文件内容格式错误，需为JSON数组");
        }

        if (CollUtil.isEmpty(jsonArray)) {
            throw new BizException("文件内容为空");
        }

        List<BaseRuleDataDetail> detailList = new ArrayList<>();
        for (Object obj : jsonArray) {
            String rowJson = JSONUtil.toJsonStr(obj);
            BaseRuleDataDetail detail = BaseRuleDataDetail.builder()
                    .ruleDataId(ruleDataId)
                    .encryptData(SecurityUtils.importantDataEncryptRsa(rowJson))
                    .build();
            detailList.add(detail);
        }

        saveBatch(detailList);
    }

    /**
     * 导出数据详情（解密后以JSON文件形式导出）
     * @param response 请求
     * @param ruleDataId 规则数据ID
     */
    public void exportDetail(HttpServletResponse response, Long ruleDataId) {
        List<BaseRuleDataDetail> details = list(new LambdaQueryWrapper<BaseRuleDataDetail>()
                .eq(BaseRuleDataDetail::getRuleDataId, ruleDataId)
                .orderByDesc(BaseRuleDataDetail::getCreateDate));

        JSONArray jsonArray = new JSONArray();
        for (BaseRuleDataDetail detail : details) {
            String decrypted = decryptSafely(detail.getEncryptData());
            if (StrUtil.isNotBlank(decrypted)) {
                jsonArray.add(JSONUtil.parse(decrypted));
            }
        }

        try {
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/json");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncodeUtil.encode("规则数据导出") + ".json");
            response.getOutputStream().write(jsonArray.toString().getBytes(StandardCharsets.UTF_8));
            response.flushBuffer();
        } catch (IOException e) {
            throw new BizException("导出失败");
        }
    }

    /**
     * 清空规则数据下的所有详情
     * @param ruleDataId 规则数据ID
     */
    @Transactional(rollbackFor = Exception.class)
    public void clearDetail(Long ruleDataId) {
        if (ruleDataId == null) {
            throw new BizException("规则数据ID不能为空");
        }
        remove(new LambdaQueryWrapper<BaseRuleDataDetail>().eq(BaseRuleDataDetail::getRuleDataId, ruleDataId));
    }

    /**
     * 安全解密，解密失败时返回原文提示，避免影响列表展示
     */
    private String decryptSafely(String encryptData) {
        if (StrUtil.isBlank(encryptData)) {
            return "";
        }
        try {
            return SecurityUtils.importantDataDecryptRsa(encryptData);
        } catch (Exception e) {
            return "[解密失败]";
        }
    }
}
