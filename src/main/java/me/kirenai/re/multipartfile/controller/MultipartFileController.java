package me.kirenai.re.multipartfile.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;


@RestController
@RequestMapping("/api/v0/multipartfile")
public class MultipartFileController {

    @GetMapping("/load")
    public ResponseEntity<String> getFile(@RequestPart("file") MultipartFile multipartFile) {
        System.out.println(multipartFile.getContentType());
        System.out.println(multipartFile.getName());
        System.out.println(multipartFile.getOriginalFilename());
        System.out.println(multipartFile.getSize());
        File file = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(multipartFile.getBytes());
            fileOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(multipartFile.getName());
    }
}
