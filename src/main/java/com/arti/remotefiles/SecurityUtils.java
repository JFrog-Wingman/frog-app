package com.arti.remotefiles;

import java.io.File;

/**
 * Security utility class for file access validation.
 * Contains methods to prevent path traversal attacks (CVE-2025-48924).
 */
public class SecurityUtils {
    
    /**
     * Path traversal detection for secure file access.
     * Validates that a filename resolves to a file within the specified base directory.
     * 
     * @param baseDir The base directory path
     * @param filename The filename to validate
     * @return true if the file access is safe, false otherwise
     */
    public static boolean isSafeFileAccess(String baseDir, String filename) {
        try {
            // Normalize and get canonical paths to handle symbolic links and path traversal
            File base = new File(baseDir).getCanonicalFile();
            File file = new File(baseDir, filename).getCanonicalFile();
            
            // Get canonical paths for comparison
            String basePath = base.getCanonicalPath();
            String filePath = file.getCanonicalPath();
            
            // Normalize path separators for cross-platform compatibility
            basePath = basePath.replace('\\', '/');
            filePath = filePath.replace('\\', '/');
            
            // Ensure the file path starts with base path and is properly contained
            // Handle both files directly in base directory and subdirectories
            return filePath.equals(basePath) || filePath.startsWith(basePath + "/");
        } catch (Exception e) {
            System.err.println("Error occurred during file access validation: " + e.getMessage());
            return false;
        }
    }
}