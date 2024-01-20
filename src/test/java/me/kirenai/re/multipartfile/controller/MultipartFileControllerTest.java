package me.kirenai.re.multipartfile.controller;

import me.kirenai.re.multipartfile.service.MultipartFileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = MultipartFileController.class)
class MultipartFileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MultipartFileService multipartFileService;

    @Test
    void createFileTest() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "file.pdf",
                MediaType.APPLICATION_PDF_VALUE,
                "Nombre: Test".getBytes(StandardCharsets.UTF_8)
        );

        when(this.multipartFileService.createFileWeb(any())).thenReturn(file.getName());

        MockMultipartHttpServletRequestBuilder request = MockMvcRequestBuilders
                .multipart("/api/v0/multipartfile/load")
                .file(file);

        this.mockMvc
                .perform(request)
                .andExpect(status().isOk())
                .andExpect(content().string(file.getName()));
    }

}