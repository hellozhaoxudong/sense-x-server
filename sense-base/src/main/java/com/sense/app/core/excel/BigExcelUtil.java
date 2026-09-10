package com.sense.app.core.excel;

import cn.hutool.core.net.URLEncodeUtil;
import com.sense.app.core.exception.BizException;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.fesod.sheet.ExcelWriter;
import org.apache.fesod.sheet.FesodSheet;
import org.apache.fesod.sheet.support.ExcelTypeEnum;
import org.apache.fesod.sheet.write.metadata.WriteSheet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BigExcelUtil {

    /**
     * 导出Excel
     * @param response  请求
     * @param fileName  文件名
     * @param heads 表头
     * @param datas 多行数据
     */
    public static void exportExcel(HttpServletResponse response, String fileName, List<Object> head, List<List<Object>> datas){
        response.setCharacterEncoding("UTF-8");
        response.setHeader("content-Type", "application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncodeUtil.encode(fileName) + ".xlsx");
        List<List<Object>> heads = new ArrayList<>();
        heads.add(head);

        try {
            ExcelWriter writer = FesodSheet.write(response.getOutputStream()).excelType(ExcelTypeEnum.XLSX).build();

            WriteSheet sheet = FesodSheet.writerSheet("Sheet1").needHead(false).build();

            writer.write(heads, sheet);
            writer.write(datas, sheet);

            writer.finish();
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
            throw new BizException("写入请求失败");
        }
    }
}
