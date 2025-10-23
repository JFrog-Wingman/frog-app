package com.arti.remotefiles;

import org.apache.struts2.ActionSupport;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class DownloadAction extends ActionSupport {

    private static final long serialVersionUID = 1L;
    private String filename;
    private InputStream inputStream;

    public String execute() throws Exception {
        String uploadDir = "webapps/ROOT/uploads";
        File fileToDownload = new File(uploadDir, filename);
        inputStream = new FileInputStream(fileToDownload);
        return SUCCESS;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public InputStream getInputStream() {
        return inputStream;
    }
}
