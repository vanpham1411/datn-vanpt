package com.example.datn.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
@Slf4j
public class StorageService {

    public String uploadFile(MultipartFile file) {
        String imageURL = null;
        log.info("productItem: {}", file);
        String pathNewDest = null;
        if (!file.isEmpty()) {
            try {

                String orgName = file.getOriginalFilename();
                pathNewDest = "/home/vanpt/IdeaProjects/datn-project/frontent/foodmanagement/public/data/" + orgName;
                File newDest = new File(pathNewDest);
                file.transferTo(newDest);
                imageURL = orgName;
            } catch (IOException e) {
                log.error("error: {}", e);
            }

        }
        return imageURL;
    }

}
