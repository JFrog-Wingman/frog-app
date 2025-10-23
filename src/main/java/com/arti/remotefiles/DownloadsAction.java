package com.arti.remotefiles;

import com.opensymphony.xwork2.ActionSupport;
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
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(baos);

        for (String filename : filenames) {
            File fileToDownload = new File(uploadDir, filename);
            if (fileToDownload.exists()) {
                FileInputStream fis = new FileInputStream(fileToDownload);
                zos.putNextEntry(new ZipEntry(filename));

                byte[] buffer = new byte[1024];
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, length);
                }
                fis.close();
                zos.closeEntry();
            }
        }
        zos.close();
        inputStream = new ByteArrayInputStream(baos.toByteArray());
        return SUCCESS;
    }

    public List<String> getFilenames() {
        return filenames;
    }

    public void setFilenames(List<String> filenames) {
        // CVE-2025-48924 resilience: Validate input lengths to prevent StackOverflowError in framework processing
        if (filenames != null) {
            for (String filename : filenames) {
                if (filename != null && filename.length() > 255) {
                    throw new IllegalArgumentException("Filename too long (max 255 characters): " + filename);
                }
            }
        }
        this.filenames = filenames;
    }

    public InputStream getInputStream() {
        return inputStream;
    }
}
