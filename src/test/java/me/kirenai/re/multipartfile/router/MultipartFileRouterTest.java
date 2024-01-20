package me.kirenai.re.multipartfile.router;

import me.kirenai.re.multipartfile.dto.FormularioDto;
import me.kirenai.re.multipartfile.handler.MultipartFileHandler;
import me.kirenai.re.multipartfile.service.MultipartFileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(value = MultipartFileRouter.class)
@ContextConfiguration(classes = {MultipartFileRouter.class, MultipartFileHandler.class})
class MultipartFileRouterTest {

    @Autowired
    private WebTestClient client;

    @MockBean
    private MultipartFileService multipartFileService;

    @Test
    void createLoadRouterTest() throws IOException {
        MockMultipartFile file = new MockMultipartFile(
                "file2",
                "file2.pdf",
                MediaType.APPLICATION_PDF_VALUE,
                "Nombre: Test".getBytes(StandardCharsets.UTF_8)
        );

        when(this.multipartFileService.createFileWebFlux(any())).thenReturn(Mono.just(file.getName()));

        MultipartBodyBuilder multipartBodyBuilder = new MultipartBodyBuilder();
        multipartBodyBuilder.part("file", new ByteArrayResource(file.getBytes())).contentType(MediaType.MULTIPART_FORM_DATA);
        multipartBodyBuilder.part("formulario", new FormularioDto()).contentType(MediaType.APPLICATION_JSON);

        this.client
                .post()
                .uri("/load")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(multipartBodyBuilder.build()))
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo(file.getName());
    }

}