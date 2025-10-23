package com.arti.remotefiles;

import com.opensymphony.xwork2.ActionSupport;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class DownloadAction extends ActionSupport {

    private static final long serialVersionUID = 1L;
    private static final String UPLOAD_DIR = "webapps/ROOT/uploads";
    private String filename;
    private InputStream inputStream;

    public String execute() throws Exception {
        // Input validation for filename
        if (filename == null || filename.trim().isEmpty()) {
            addActionError("No filename provided");
            return ERROR;
        }
        
        // Path traversal protection - same as UploadAction
        if (!UploadAction.isSafeFileAccess(UPLOAD_DIR, filename)) {
            System.err.println("Security violation: Malicious download filename blocked - " + filename);
            addActionError("Invalid file path detected");
            return ERROR;
        }
        
        File fileToDownload = new File(UPLOAD_DIR, filename);
        
        // Additional security checks
        if (!fileToDownload.exists()) {
            addActionError("File not found");
            return ERROR;
        }
        
        if (!fileToDownload.isFile()) {
            addActionError("Invalid file");
            return ERROR;
        }
        
        // Final canonical path check
        try {
            File uploadDirectory = new File(UPLOAD_DIR).getCanonicalFile();
            if (!fileToDownload.getCanonicalPath().startsWith(uploadDirectory.getCanonicalPath())) {
                System.err.println("Path traversal attempt in download: " + filename);
                addActionError("Access denied");
                return ERROR;
            }
        } catch (Exception e) {
            System.err.println("Error validating download path: " + e.getMessage());
            addActionError("Access denied");
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
