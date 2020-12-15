package com.ai.generator.file;

public class FileUploadOptions {
    private String imageUploadPathRoot;
    private long maxUploadSize ;

    public String getImageUploadPathRoot() {
        return imageUploadPathRoot;
    }

    public void setImageUploadPathRoot(String imageUploadPathRoot) {
        this.imageUploadPathRoot = imageUploadPathRoot;
    }

    public long getMaxUploadSize() {
        return maxUploadSize;
    }

    public void setMaxUploadSize(long maxUploadSize) {
        this.maxUploadSize = maxUploadSize;
    }
}
