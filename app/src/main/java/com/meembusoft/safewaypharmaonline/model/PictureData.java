package com.meembusoft.safewaypharmaonline.model;

import java.io.File;

public class PictureData {

    private String fileName = "";
    private File file;

    public PictureData(String fileName, File file) {
        this.fileName = fileName;
        this.file = file;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return "PictureData{" +
                "fileName='" + fileName + '\'' +
                ", file=" + file +
                '}';
    }
}
