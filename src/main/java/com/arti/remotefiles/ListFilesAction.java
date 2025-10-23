package com.arti.remotefiles;

import org.apache.struts2.ActionSupport;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ListFilesAction extends ActionSupport {

    private static final long serialVersionUID = 1L;
    private List<String> files;

    public String execute() {
        String uploadDir = "webapps/ROOT/uploads";
        File uploadDirectory = new File(uploadDir);
        if (uploadDirectory.exists() && uploadDirectory.isDirectory()) {
            files = new ArrayList<String>();
            for (File file : uploadDirectory.listFiles()) {
                files.add(file.getName());
            }
        }
        return SUCCESS;
    }

    public List<String> getFiles() {
        return files;
    }
}
