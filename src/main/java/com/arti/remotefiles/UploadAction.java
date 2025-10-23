package com.arti.remotefiles;

import com.opensymphony.xwork2.ActionSupport;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class UploadAction extends ActionSupport {
    private static final long serialVersionUID = 1L;
    
    // Enhanced path traversal detection for secure file upload
    public static boolean isSafeFileAccess(String baseDir, String filename) {
        if (filename == null || filename.trim().isEmpty()) {
            System.err.println("Invalid filename: null or empty");
            return false;
        }
        
        // Reject filenames with dangerous characters or patterns
        if (filename.contains("..") || filename.contains("/") || filename.contains("\\") 
            || filename.contains("\0") || filename.startsWith(".")) {
            System.err.println("Dangerous filename detected: " + filename);
            return false;
        }
        
        try {
            File base = new File(baseDir).getCanonicalFile();
            File file = new File(baseDir, filename).getCanonicalFile();
            
            // Ensure the file is within the base directory
            boolean withinBase = file.getPath().startsWith(base.getPath() + File.separator);
            
            // Additional check: ensure the resolved file is still within bounds
            if (!withinBase || !file.getParent().equals(base.getPath())) {
                System.err.println("File outside allowed directory: " + filename);
                return false;
            }
            
            return true;
        } catch (Exception e) {
            System.err.println("Error occurred during path validation: " + e.getMessage());
            return false;
        }
    }
    
    // Additional filename sanitization
    private static String sanitizeFilename(String filename) {
        if (filename == null) return null;
        
        // Remove dangerous characters and normalize
        String sanitized = filename.replaceAll("[^a-zA-Z0-9._-]", "_");
        
        // Prevent double extensions and hidden files
        if (sanitized.startsWith(".") || sanitized.contains("..")) {
            sanitized = "safe_" + sanitized.replaceAll("\\.", "_");
        }
        
        // Limit filename length
        if (sanitized.length() > 255) {
            sanitized = sanitized.substring(0, 255);
        }
        
        return sanitized;
    }

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
                    uploadDirectory.mkdirs(); // Use mkdirs for better directory creation
                }
                
                // Sanitize filename first
                String sanitizedFilename = sanitizeFilename(uploadFileName);
                if (sanitizedFilename == null || sanitizedFilename.trim().isEmpty()) {
                    addActionError("Invalid filename provided");
                    return ERROR;
                }
                
                // Path traversal check with enhanced validation
                if (!isSafeFileAccess(uploadDir, sanitizedFilename)) {
                    System.err.println("Security violation: Malicious filename blocked - " + uploadFileName);
                    addActionError("Invalid file path detected");
                    return ERROR;
                }
                
                // Additional file size check (limit to 10MB)
                if (upload.length() > 10 * 1024 * 1024) {
                    addActionError("File too large. Maximum size is 10MB");
                    return ERROR;
                }
                
                File destFile = new File(uploadDirectory, sanitizedFilename);
                
                // Final safety check before writing
                if (!destFile.getCanonicalPath().startsWith(uploadDirectory.getCanonicalPath())) {
                    System.err.println("Final path validation failed for: " + sanitizedFilename);
                    addActionError("Invalid file path");
                    return ERROR;
                }
                
                try (FileInputStream inputStream = new FileInputStream(upload);
                     FileOutputStream outputStream = new FileOutputStream(destFile)) {
                    
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = inputStream.read(buffer)) > 0) {
                        outputStream.write(buffer, 0, length);
                    }
                }
                
                System.out.println("File uploaded successfully: " + sanitizedFilename);
                return SUCCESS;
                
            } catch (IOException e) {
                System.err.println("File upload failed: " + e.getMessage());
                addActionError("File upload failed: " + e.getMessage());
                return ERROR;
            }
        } else {
            addActionError("No file uploaded.");
            return ERROR;
        }
    }
}