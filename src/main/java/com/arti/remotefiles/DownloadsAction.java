package com.arti.remotefiles;

import org.apache.struts2.ActionSupport;
import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class DownloadsAction extends ActionSupport {

    private static final long serialVersionUID = 1L;
    private List<String> filenames;
    private InputStream inputStream;

    public String execute() throws Exception {
        String uploadDir = "webapps/ROOT/uploads";
        
        // Pre-validate all filenames to prevent partial ZIP creation
        if (filenames != null) {
            for (String filename : filenames) {
                if (!SecurityUtils.isSafeFileAccess(uploadDir, filename)) {
                    System.err.println("Security violation: Invalid filename attempted - " + filename);
                    addActionError("Invalid file selection. Please check your file choices.");
                    return ERROR;
                }
                
                File fileToDownload = new File(uploadDir, filename);
                if (!fileToDownload.exists() || !fileToDownload.isFile()) {
                    System.err.println("File not found: " + filename);
                    addActionError("One or more selected files are not available.");
                    return ERROR;
                }
            }
        }
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(baos);

        for (String filename : filenames) {
            File fileToDownload = new File(uploadDir, filename);
            FileInputStream fis = new FileInputStream(fileToDownload);
            // Sanitize the ZIP entry name to prevent zip slip
            String sanitizedName = new File(filename).getName();
            zos.putNextEntry(new ZipEntry(sanitizedName));

            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, length);
            }
            fis.close();
            zos.closeEntry();
        }
        zos.close();
        inputStream = new ByteArrayInputStream(baos.toByteArray());
        return SUCCESS;
    }

    public List<String> getFilenames() {
        return filenames;
    }

    public void setFilenames(List<String> filenames) {
        this.filenames = filenames;
    }

    public InputStream getInputStream() {
        return inputStream;
    }
}
