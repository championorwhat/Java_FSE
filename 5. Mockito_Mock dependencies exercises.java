import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

/**
 * Solutions for mocking dependencies in Spring tests using Mockito.
 */

// --- Domain / repository / service / controller ---

class MockitoDepsUser {
    Long id;
    String name;

    MockitoDepsUser() {}

    MockitoDepsUser(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    Long getId() { return id; }
    void setId(Long id) { this.id = id; }

    String getName() { return name; }
    void setName(String name) { this.name = name; }
}

interface MockitoDepsUserRepository extends JpaRepository<MockitoDepsUser, Long> { }

@Service
class MockitoDepsUserService {
    @Autowired
    private MockitoDepsUserRepository userRepository;

    MockitoDepsUser getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
}

@RestController
@RequestMapping("/users")
class MockitoDepsUserController {
    @Autowired
    private MockitoDepsUserService userService;

    @GetMapping("/{id}")
    ResponseEntity<MockitoDepsUser> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }
}

// Exercise 1: Mocking a Service Dependency in a Controller Test
@WebMvcTest(MockitoDepsUserController.class)
class MockitoDepsControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    MockitoDepsUserService userService;

    @Test
    void getUser_shouldUseMockedService() throws Exception {
        when(userService.getUserById(1L)).thenReturn(new MockitoDepsUser(1L, "Alice"));

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Alice"));
    }
}

// Exercise 2: Mocking a Repository in a Service Test
@ExtendWith(MockitoExtension.class)
class MockitoDepsServiceTest {
    @Mock
    MockitoDepsUserRepository userRepository;

    @InjectMocks
    MockitoDepsUserService userService;

    @Test
    void getUserById_shouldReturnUserFromMockRepository() {
        MockitoDepsUser user = new MockitoDepsUser(1L, "John");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        MockitoDepsUser result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals("John", result.getName());
    }
}

// Exercise 3: Mocking a Service Dependency in an Integration Test
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
class MockitoDepsIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    MockitoDepsUserService userService;

    @Test
    void integrationTest_shouldReachControllerWithMockedService() throws Exception {
        when(userService.getUserById(7L)).thenReturn(new MockitoDepsUser(7L, "Bob"));

        mockMvc.perform(get("/users/7"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Bob"));
    }
}
