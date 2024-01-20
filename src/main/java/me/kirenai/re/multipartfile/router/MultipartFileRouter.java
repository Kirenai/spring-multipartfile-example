package me.kirenai.re.multipartfile.router;

import me.kirenai.re.multipartfile.handler.MultipartFileHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class MultipartFileRouter {

    @Bean
    public RouterFunction<ServerResponse> router(MultipartFileHandler handler) {
        return RouterFunctions.route(RequestPredicates.POST("/load")
                .and(RequestPredicates.contentType(MediaType.MULTIPART_FORM_DATA)), handler::create);
    }

}
