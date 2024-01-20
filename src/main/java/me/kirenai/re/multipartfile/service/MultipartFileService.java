package me.kirenai.re.multipartfile.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.kirenai.re.multipartfile.dto.FormularioDto;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.FormFieldPart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@Service
public class MultipartFileService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String createFileWeb(MultipartFile multipartFile) {
        System.out.println(multipartFile.getContentType());
        System.out.println(multipartFile.getName());
        System.out.println(multipartFile.getOriginalFilename());
        System.out.println(multipartFile.getSize());
        File file = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(multipartFile.getBytes()); //Bytes
            fileOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return multipartFile.getName();
    }

    public Mono<String> createFileWebFlux(Map<String, Part> map) {
        FilePart filePart = ((FilePart) map.get("file"));
        FormFieldPart formFieldPart = ((FormFieldPart) map.get("formulario"));
        FormularioDto formularioDto;

        try {
            formularioDto = this.objectMapper.readValue(formFieldPart.value(), FormularioDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        System.out.println(formularioDto);
        System.out.println(filePart.filename());

        Mono<byte[]> monoBytes = DataBufferUtils.join(filePart.content()) //Bytes
                .map(dataBuffer -> {
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    return bytes;
                });

        File file = new File(filePart.filename());

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            monoBytes.subscribe(bytes -> {
                try {
                    fileOutputStream.write(bytes);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            fileOutputStream.close();
            return Mono.just(filePart.filename());
        } catch (IOException e) {
            return Mono.error(new RuntimeException(e));
        }
    }

}
