package com.ai.generator.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by maofw on 2019-03-19.
 */
@Component
public class PropertiesConfig {
    @Value("${image.httpRoot}")
    private String imageHttpRoot ;
    @Value("${image.uploadPathRoot}")
    private String imageUploadPathRoot ;

    @Value("${image.fileSize}")
    private Long imageFileSize ;

    public String getImageHttpRoot() {
        return imageHttpRoot;
    }

    public void setImageHttpRoot(String imageHttpRoot) {
        this.imageHttpRoot = imageHttpRoot;
    }

    public String getImageUploadPathRoot() {
        return imageUploadPathRoot;
    }

    public void setImageUploadPathRoot(String imageUploadPathRoot) {
        this.imageUploadPathRoot = imageUploadPathRoot;
    }

    public Long getImageFileSize() {
        return imageFileSize;
    }

    public void setImageFileSize(Long imageFileSize) {
        this.imageFileSize = imageFileSize;
    }
}
