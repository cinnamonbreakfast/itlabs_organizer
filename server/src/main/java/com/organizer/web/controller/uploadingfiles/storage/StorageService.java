package com.organizer.web.controller.uploadingfiles.storage;

import com.organizer.core.model.Image;
import com.organizer.core.repository.Repository;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public interface StorageService extends Repository<Long,Image> {

    void store(MultipartFile file);

    @Query("select i from Image i ")
    Stream<Path> loadAll();

    @Query("select i.name from Image i where i.name=lower(?1) ")
    Resource loadAsResource(String filename);

}