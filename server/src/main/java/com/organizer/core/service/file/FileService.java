package com.organizer.core.service.file;

import org.hibernate.dialect.identity.SybaseAnywhereIdentityColumnSupport;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileService {
    public String uploadDir=Paths.get("").toAbsolutePath().toString()+"\\src\\main\\resources\\images";

    public void uploadDir(MultipartFile file,String genName,String username){
        try {
            Path copyLocation = Paths
                    .get(uploadDir + File.separator + genName);

            File[] listOfFiles = new File(uploadDir).listFiles();

            for(File f :listOfFiles)
            {
                if(f.getName().split("[.]")[0].equals(username)){
                    System.out.println("JAVA IS SO DRUNK");

                    Files.delete(f.toPath());
                }
            }
            Files.copy(file.getInputStream(),copyLocation);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Resource loadFileAsResource(String fileName) {

        File f = new File(uploadDir);
        File[] matchingFiles = f.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.startsWith(fileName)||name.endsWith(fileName);
            }
        });
        String pathFile = matchingFiles[0].getAbsolutePath();

        try {
            Path filePath = Paths.get(pathFile);
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                return null;
            }
        } catch (MalformedURLException ex) {
            return null;
        }
    }
}
