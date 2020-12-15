package com.ai.generator.controller;

import com.ai.generator.http.HttpResult;
import com.ai.generator.model.FileResponse;
import com.ai.generator.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class FileUploadController {
    @Autowired
    private FileService fileService;

    /**
     * 上传图片
     */
    @PostMapping(value = "/file/uploadObjectOSS", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public HttpResult<FileResponse> uploadObjectOSS(MultipartFile file) throws IOException {
        return fileService.upload(file);
    }

    /**
     * 上传文件(多文件)
     */
    @PostMapping(value = "/file/uploadObjectOSSMul", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public HttpResult<List<FileResponse>> uploadObjectOSSMul(@RequestPart MultipartFile[] files) throws IOException {
        List<FileResponse> fileList = null ;
        if(files!=null && files.length>0){
            fileList = new ArrayList<>();
            for(MultipartFile file:files){
                HttpResult<FileResponse> result = fileService.upload(file);
                if(!result.isSuccess()){
                    //如果有一个文件失败了则清理之前存储的文件
                    if(fileList!=null && fileList.size()>0){
                        for(FileResponse file1:fileList){
                            fileService.delete(file1.getPath());
                        }
                    }
                    return HttpResult.error(result.getMsg());
                }
                fileList.add(result.getData());
            }
        }
        return HttpResult.success(fileList);
    }
    /**
     * 删除图片
     */
    @PostMapping(value = "/file/deleteOSS", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public HttpResult<String> deleteOSS(@RequestParam String filePath) throws IOException {
        return fileService.delete(filePath);
    }


    /**
     * 获取图片文件
     */
    @PostMapping(value = "/file/fetch")
    public HttpResult<byte[]> fetch(@RequestParam String filePath) throws IOException {
        return fileService.fetch(filePath);
    }
}
