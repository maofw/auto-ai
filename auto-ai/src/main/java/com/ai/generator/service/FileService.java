package com.ai.generator.service;

import com.ai.generator.http.HttpResult;
import com.ai.generator.model.FileResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author Administrator
 */
public interface FileService {


    /**
     * 文件上传
     *
     * @return
     */
    HttpResult<FileResponse> upload(MultipartFile file) throws IOException;


    /**
     * 文件删除
     *
     * @return
     */
    HttpResult delete(String filePath) throws IOException;

    HttpResult fetch(String filePath) throws IOException ;
}
