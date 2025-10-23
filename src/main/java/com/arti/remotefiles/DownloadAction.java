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
        if (filename == null || filename.trim().isEmpty()) {
            addActionError("No filename specified.");
            return ERROR;
        }
        
        String uploadDir = "webapps/ROOT/uploads";
        
        // CVE-2025-48924 fix: Validate filename to prevent path traversal
        if (!SecurityUtils.isSafeFileAccess(uploadDir, filename)) {
            System.err.println("Security violation: Invalid filename attempted - " + filename);
            addActionError("Invalid file request. Access denied.");
            return ERROR;
        }
        
        File fileToDownload = new File(uploadDir, filename);
        
        if (!fileToDownload.exists() || !fileToDownload.isFile()) {
            addActionError("File not found: " + filename);
            return ERROR;
        }
        
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
