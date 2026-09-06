package com.guilhermedev.todolist.integration;

import com.guilhermedev.todolist.dto.login.LoginRequestDTO;
import com.guilhermedev.todolist.dto.login.LoginResponseDTO;
import com.guilhermedev.todolist.dto.login.RegisterRequestDTO;
import com.guilhermedev.todolist.dto.task.TaskRequestDTO;
import com.guilhermedev.todolist.dto.task.TaskResponseDTO;
import com.guilhermedev.todolist.dto.category.CategoryRequestDTO;
import com.guilhermedev.todolist.dto.category.CategoryResponseDTO;
import com.guilhermedev.todolist.enums.Priority;
import com.guilhermedev.todolist.enums.Status;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TaskIntegrationTest {

    @LocalServerPort
    private int port;

    private final RestTemplate rest = new RestTemplate(new HttpComponentsClientHttpRequestFactory());

    private String base() { return "http://localhost:" + port; }

    private String registerAndGetToken() {
        RegisterRequestDTO register = new RegisterRequestDTO();
        register.setName("Task Tester");
        register.setEmail("task@test.com");
        register.setPassword("password");
        rest.postForEntity(base() + "/auth/register", register, RegisterRequestDTO.class);

        LoginRequestDTO login = new LoginRequestDTO();
        login.setEmail("task@test.com");
        login.setPassword("password");
        ResponseEntity<LoginResponseDTO> resp = rest.postForEntity(base() + "/auth/login", login, LoginResponseDTO.class);
        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertNotNull(resp.getBody());
        return resp.getBody().getToken();
    }

    @Test
    void fullTaskFlow() {
        String token = registerAndGetToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // create category
        CategoryRequestDTO creq = new CategoryRequestDTO();
        creq.setName("Personal");
        creq.setColor("#00ff00");
        HttpEntity<CategoryRequestDTO> cent = new HttpEntity<>(creq, headers);
        ResponseEntity<CategoryResponseDTO> cResp = rest.exchange(base() + "/api/categories", HttpMethod.POST, cent, CategoryResponseDTO.class);
        assertEquals(HttpStatus.CREATED, cResp.getStatusCode());
        Long catId = cResp.getBody().getId();

        // create task
        TaskRequestDTO treq = new TaskRequestDTO();
        treq.setTitle("Buy milk");
        treq.setDescription("Buy 2 liters");
        treq.setPriority(Priority.MEDIA);
                treq.setStatus(Status.PENDENTE);
        treq.setDueDate(LocalDate.now().plusDays(2));
        treq.setCategoryId(catId);

        HttpEntity<TaskRequestDTO> tent = new HttpEntity<>(treq, headers);
        ResponseEntity<TaskResponseDTO> tResp = rest.exchange(base() + "/api/tasks", HttpMethod.POST, tent, TaskResponseDTO.class);
        assertEquals(HttpStatus.CREATED, tResp.getStatusCode());
        Long taskId = tResp.getBody().getId();

        // get task
        ResponseEntity<TaskResponseDTO> getResp = rest.exchange(base() + "/api/tasks/" + taskId, HttpMethod.GET, new HttpEntity<>(headers), TaskResponseDTO.class);
        assertEquals(HttpStatus.OK, getResp.getStatusCode());
        assertEquals("Buy milk", getResp.getBody().getTitle());

        // mark complete
        ResponseEntity<TaskResponseDTO> compResp = rest.exchange(base() + "/api/tasks/" + taskId + "/complete", HttpMethod.PATCH, new HttpEntity<>(headers), TaskResponseDTO.class);
        assertEquals(HttpStatus.OK, compResp.getStatusCode());
        assertEquals(Status.CONCLUIDA, compResp.getBody().getStatus());

        // delete
        ResponseEntity<Void> delResp = rest.exchange(base() + "/api/tasks/" + taskId, HttpMethod.DELETE, new HttpEntity<>(headers), Void.class);
        assertEquals(HttpStatus.NO_CONTENT, delResp.getStatusCode());
    }
}
