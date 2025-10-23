package com.arti.remotefiles;

import org.apache.struts2.ActionSupport;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UploadAction extends ActionSupport {
    // Path traversal detection for secure file upload
    public static boolean isSafeFileAccess(String baseDir, String filename) {
        try {
            File base = new File(baseDir).getCanonicalFile();
            File file = new File(baseDir, filename).getCanonicalFile();
            return file.getPath().startsWith(base.getPath() + File.separator);
        } catch (Exception e) {
            System.err.println("Error occurred: " + e.getMessage());
            return false;
        }
    }

    private static final long serialVersionUID = 1L;


    private File upload;
    private String uploadContentType;
    private String uploadFileName;


    public File getUpload() {
        return upload;
    }

    public void setUpload(File upload) {
        this.upload = upload;
    }

    public String getUploadContentType() {
        return uploadContentType;
    }

    public void setUploadContentType(String uploadContentType) {
        this.uploadContentType = uploadContentType;
    }

    public String getUploadFileName() {
        return uploadFileName;
    }

    public void setUploadFileName(String uploadFileName) {
        this.uploadFileName = uploadFileName;
    }


    public String doUpload() {
        if (upload != null && upload.length() > 0) {
            try {
                String uploadDir = "webapps/ROOT/uploads";
                File uploadDirectory = new File(uploadDir);
                if (!uploadDirectory.exists()) {
                    uploadDirectory.mkdir();
                }
                // Path traversal check
                if (!isSafeFileAccess(uploadDir, uploadFileName)) {
                    System.err.println("Identified malicious filename - " + uploadFileName);
                    return ERROR;
                }
                File destFile = new File(uploadDirectory, uploadFileName);
                FileInputStream inputStream = new FileInputStream(upload);
                FileOutputStream outputStream = new FileOutputStream(destFile);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
                inputStream.close();
                outputStream.close();
                return SUCCESS;
            } catch (IOException e) {
                addActionError("File upload failed: " + e.getMessage());
                return ERROR;
            }
        } else {
            addActionError("No file uploaded.");
            return ERROR;
        }
    }

}