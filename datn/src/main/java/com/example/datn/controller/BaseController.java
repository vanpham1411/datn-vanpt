package com.example.datn.controller;

import com.example.datn.service.StorageService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.dynamic.ClassFileLocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Collections;

@RestController
@Slf4j
@CrossOrigin
public class BaseController {

    @Autowired
    StorageService storageService;


    @RequestMapping(value = "/File", method = RequestMethod.POST, consumes = { "multipart/form-data" })
    public ResponseEntity uploadFile(@ModelAttribute MultipartFile file) throws IOException {

        return ResponseEntity.ok(new ResponseBody(
                storageService.uploadFile(file),
                ResponseBody.Status.SUCCESS,
                ResponseBody.Code.SUCCESS
        ));
    }
@Data
@AllArgsConstructor
@NoArgsConstructor
    public class FormFile {
        String filename;
        String name;
        byte[] File;
    }

public interface IFormFile {

    public String getFilename();

    public String getContentType();

    public byte[] getBytes();
}
}
