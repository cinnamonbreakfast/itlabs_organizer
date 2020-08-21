package com.organizer.web.controller;

import com.organizer.core.service.file.FileService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.core.io.Resource;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

@Controller
public class FileController {
    private final FileService fileService ;
    @Autowired
    public FileController(FileService fileService){
        this.fileService=fileService;
    }

    @RequestMapping(value ="img/{image_url}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String image_url, HttpServletRequest request)
    {
        Resource resource=null;
        try{
        resource = fileService.loadFileAsResource(image_url);
        }
        catch (Exception e){
            resource  = fileService.loadFileAsResource("default_company.png");
        }
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
            .body(resource);
    }
}
