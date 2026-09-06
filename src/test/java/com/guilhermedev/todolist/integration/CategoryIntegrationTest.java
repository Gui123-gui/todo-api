package com.guilhermedev.todolist.integration;

import com.guilhermedev.todolist.dto.category.CategoryRequestDTO;
import com.guilhermedev.todolist.dto.category.CategoryResponseDTO;
import com.guilhermedev.todolist.dto.login.LoginRequestDTO;
import com.guilhermedev.todolist.dto.login.LoginResponseDTO;
import com.guilhermedev.todolist.dto.login.RegisterRequestDTO;
import com.guilhermedev.todolist.dto.login.RegisterResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CategoryIntegrationTest extends IntegrationTestBase {

    @LocalServerPort
    private int port;

    private final RestTemplate rest = new RestTemplate(new HttpComponentsClientHttpRequestFactory());

    private String base() { return "http://localhost:" + port; }

    private String registerAndGetToken() {
        RegisterRequestDTO register = new RegisterRequestDTO();
        register.setName("Cat Tester");
        register.setEmail("cat@test.com");
        register.setPassword("password");
        rest.postForEntity(base() + "/auth/register", register, RegisterResponseDTO.class);

        LoginRequestDTO login = new LoginRequestDTO();
        login.setEmail("cat@test.com");
        login.setPassword("password");
        ResponseEntity<LoginResponseDTO> resp = rest.postForEntity(base() + "/auth/login", login, LoginResponseDTO.class);
        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertNotNull(resp.getBody());
        return resp.getBody().getToken();
    }

    @Test
    void fullCategoryFlow() {
        String token = registerAndGetToken();

        CategoryRequestDTO req = new CategoryRequestDTO();
        req.setName("Work");
        req.setColor("#ff0000");

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CategoryRequestDTO> entity = new HttpEntity<>(req, headers);
        ResponseEntity<CategoryResponseDTO> createResp = rest.exchange(base() + "/api/categories", HttpMethod.POST, entity, CategoryResponseDTO.class);
        assertEquals(HttpStatus.CREATED, createResp.getStatusCode());
        assertNotNull(createResp.getBody());
        Long catId = createResp.getBody().getId();

        // Get all
        HttpEntity<Void> getEntity = new HttpEntity<>(headers);
        ResponseEntity<CategoryResponseDTO[]> listResp = rest.exchange(base() + "/api/categories", HttpMethod.GET, getEntity, CategoryResponseDTO[].class);
        assertEquals(HttpStatus.OK, listResp.getStatusCode());
        assertTrue(listResp.getBody().length >= 1);

        // Update
        req.setName("Work Updated");
        HttpEntity<CategoryRequestDTO> updateEntity = new HttpEntity<>(req, headers);
        ResponseEntity<CategoryResponseDTO> updateResp = rest.exchange(base() + "/api/categories/" + catId, HttpMethod.PUT, updateEntity, CategoryResponseDTO.class);
        assertEquals(HttpStatus.OK, updateResp.getStatusCode());
        assertEquals("Work Updated", updateResp.getBody().getName());

        // Delete
        ResponseEntity<Void> delResp = rest.exchange(base() + "/api/categories/" + catId, HttpMethod.DELETE, getEntity, Void.class);
        assertEquals(HttpStatus.NO_CONTENT, delResp.getStatusCode());
    }
}
