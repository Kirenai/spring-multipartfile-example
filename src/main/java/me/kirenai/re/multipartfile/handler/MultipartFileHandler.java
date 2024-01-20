package me.kirenai.re.multipartfile.handler;

import me.kirenai.re.multipartfile.service.MultipartFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class MultipartFileHandler {

    private final MultipartFileService multipartFileService;

    @Autowired
    public MultipartFileHandler(MultipartFileService multipartFileService) {
        this.multipartFileService = multipartFileService;
    }

    public Mono<ServerResponse> create(ServerRequest request) {
        return request.body(BodyExtractors.toMultipartData())
                .flatMap(parts -> this.multipartFileService.createFileWebFlux(parts.toSingleValueMap()))
                .flatMap(filename -> ServerResponse.ok().bodyValue(filename));
    }

}
