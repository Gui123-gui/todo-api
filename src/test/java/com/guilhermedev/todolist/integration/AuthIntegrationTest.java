package com.guilhermedev.todolist.integration;

import com.guilhermedev.todolist.dto.login.LoginRequestDTO;
import com.guilhermedev.todolist.dto.login.LoginResponseDTO;
import com.guilhermedev.todolist.dto.login.RegisterRequestDTO;
import com.guilhermedev.todolist.dto.login.RegisterResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthIntegrationTest extends IntegrationTestBase {

    @LocalServerPort
    private int port;

    private final RestTemplate rest = new RestTemplate();

    @Test
    void registerAndLoginWithMySQLContainer() {
        String base = "http://localhost:" + port;

        RegisterRequestDTO register = new RegisterRequestDTO();
        register.setName("Integration Test");
        register.setEmail("it@example.com");
        register.setPassword("password");

        ResponseEntity<RegisterResponseDTO> regResp = rest.postForEntity(base + "/auth/register", register, RegisterResponseDTO.class);
        assertEquals(HttpStatus.CREATED, regResp.getStatusCode());
        assertNotNull(regResp.getBody());
        assertEquals("Integration Test", regResp.getBody().getName());

        LoginRequestDTO login = new LoginRequestDTO();
        login.setEmail("it@example.com");
        login.setPassword("password");

        ResponseEntity<LoginResponseDTO> loginResp = rest.postForEntity(base + "/auth/login", login, LoginResponseDTO.class);
        assertEquals(HttpStatus.OK, loginResp.getStatusCode());
        assertNotNull(loginResp.getBody());
        assertNotNull(loginResp.getBody().getToken());
    }
}
