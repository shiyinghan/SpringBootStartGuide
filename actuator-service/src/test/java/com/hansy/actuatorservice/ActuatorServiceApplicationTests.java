package com.hansy.actuatorservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.web.server.LocalManagementPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.assertj.core.api.BDDAssertions.then;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ActuatorServiceApplicationTests {

    @LocalServerPort
    private int port;

    @LocalManagementPort
    private int mgt;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void shouldReturn200WhenSendingRequestToController() {
        ResponseEntity<Map> entity = this.testRestTemplate.getForEntity(
                "http://localhost:" + this.port + "/hello-world", Map.class
        );
        then(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldReturn200WhenSendingRequestToManagementEndpoint() {
        ResponseEntity<Map> entity = this.testRestTemplate.getForEntity(
                "http://localhost:" + this.mgt + "/actuator/info", Map.class
        );
        then(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
