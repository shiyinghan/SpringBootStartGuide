package com.hansy.uploadingfiles;

import com.hansy.uploadingfiles.storage.StorageService;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FileUploadIntegrationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private StorageService storageService;

    @LocalServerPort
    private int port;

    @Test
    public void shouldUploadFile() {
        ClassPathResource resource = new ClassPathResource("testupload.txt", getClass());

        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("file", resource);
        ResponseEntity<String> response = restTemplate.postForEntity("/", map, String.class);

        assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.FOUND);
        assertThat(response.getHeaders().getLocation().toString()).startsWith("http://localhost:" + port);

        BDDMockito.then(storageService).should().store(Matchers.any(MultipartFile.class));
    }

    @Test
    public void shouldDownloadFile() {
        ClassPathResource resource = new ClassPathResource("testupload.txt", getClass());
        BDDMockito.given(storageService.loadAsResource("testupload.txt")).willReturn(resource);

        ResponseEntity<String> response = restTemplate.getForEntity("/files/{filename}", String.class, "testupload.txt");

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION))
                .isEqualTo("attachment; filename=\"testupload.txt\"");
        assertThat(response.getBody()).isEqualTo("Spring Framework");
    }
}
