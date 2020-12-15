package com.ai.generator.service.impl;


import com.ai.generator.http.HttpResult;
import com.ai.generator.model.FileResponse;
import com.ai.generator.service.FileService;
import com.ai.generator.util.FileUploadUtils;
import com.ai.generator.util.PropertiesConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author Administrator
 */
@Service
public class FileServiceImpl implements FileService {
    /**
     * log日志
     */
    protected static Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Autowired
    private FileUploadUtils fileUploadUtils ;
    @Autowired
    private PropertiesConfig propertiesConfig ;
    @Override
    public HttpResult<FileResponse> upload(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return HttpResult.error("文件为空");
        }

        Long size = file.getSize();
        if (size > propertiesConfig.getImageFileSize()) {
            logger.info("文件过大:" + size);
            return HttpResult.error("上传文件不能超过"+propertiesConfig.getImageFileSize()+"字节");
        }
        FileResponse mfile = null ;
        try {
            mfile = fileUploadUtils.upload(propertiesConfig.getImageUploadPathRoot(),file);
            logger.info("上传成功！");
            return HttpResult.success(mfile);
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("上传失败！");
            return HttpResult.error("上传失败");
        }
    }

    @Override
    public HttpResult delete(String filePath) throws IOException {
        if (StringUtils.isEmpty(filePath)) {
            return HttpResult.error("文件为空");
        }
        boolean b = fileUploadUtils.deleteImageOnDisk(propertiesConfig.getImageUploadPathRoot()+filePath);
        if(b){
            return HttpResult.success();
        }else{
            return HttpResult.error("文件删除失败");
        }
    }

    @Override
    public HttpResult fetch(String filePath) throws IOException {
        byte[] bytes = fileUploadUtils.getBytesByFile(propertiesConfig.getImageUploadPathRoot()+filePath);
        if(bytes!=null){
            return HttpResult.success(bytes);
        }else{
            return HttpResult.error("文件不存在");
        }
    }
}
