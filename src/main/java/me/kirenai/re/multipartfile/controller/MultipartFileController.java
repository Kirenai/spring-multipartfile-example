package me.kirenai.re.multipartfile.controller;

import me.kirenai.re.multipartfile.service.MultipartFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/v0/multipartfile")
public class MultipartFileController {

    private final MultipartFileService multipartFileService;

    @Autowired
    public MultipartFileController(MultipartFileService multipartFileService) {
        this.multipartFileService = multipartFileService;
    }

    @PostMapping("/load")
    public ResponseEntity<String> createFile(@RequestPart("file") MultipartFile multipartFile) {
        String name = this.multipartFileService.createFileWeb(multipartFile);
        return ResponseEntity.ok(name);
    }
}
