package com.ai.generator.controller;

import com.ai.generator.excel.ExcelUtil;
import com.ai.generator.http.HttpResult;
import com.ai.generator.service.BatchCurdService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public abstract class AbstractBaseController {
    /**
     * 上传excel
     * @param service
     * @param file
     * @return
     */
    protected <T> HttpResult<String> uploadExcel(BatchCurdService<T> service,  Class<T> tClass, MultipartFile file){
        List<T> list = null;
        try {
            if (file== null || file.isEmpty()) {
                return HttpResult.error("文件为空");
            }
            try {
                list = ExcelUtil.parseExcel(file,tClass);
            } catch (Exception e) {
                e.printStackTrace();
                return HttpResult.error("解析Excel出错:" + e.getMessage());
            }
            if (list == null || list.isEmpty()) {
                return HttpResult.error("解析无内容");
            }

            //插入数据
            int res = service.insertBatch(list);
            if (res > 0) {
                return HttpResult.success("导入成功");
            } else {
                return HttpResult.error("导入失败");
            }
        }finally {
            //清理数据
            if(list!=null){
                list.clear();
                list = null ;
            }
        }
    }
}
