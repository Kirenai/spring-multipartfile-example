package me.kirenai.re.multipartfile.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

@Configuration
public class MultipartFileRouter {

    @Bean
    public RouterFunction<ServerResponse> router() {
        return RouterFunctions.route(RequestPredicates.GET("/load")
                .and(RequestPredicates.contentType(MediaType.MULTIPART_FORM_DATA)), request ->
                request.body(BodyExtractors.toMultipartData()).flatMap(parts -> {
                    Map<String, Part> map = parts.toSingleValueMap();
                    FilePart filePart = ((FilePart) map.get("file"));

                    System.out.println(filePart.filename());
                    Mono<byte[]> monoBytes = DataBufferUtils.join(filePart.content())
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
                    } catch (IOException e) {
                        return Mono.error(new RuntimeException(e));
                    }

                    return Mono.empty();
                }));
    }

}
