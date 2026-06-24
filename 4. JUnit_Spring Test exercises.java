import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

/**
 * Combined Spring/JUnit testing solutions.
 *
 * These examples are intentionally self-contained and use a single User model
 * across the exercises.
 */

// --- Domain / repository / service / controller ---

class SpringTestUser {
    Long id;
    String name;

    SpringTestUser() {}

    SpringTestUser(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    Long getId() { return id; }
    void setId(Long id) { this.id = id; }

    String getName() { return name; }
    void setName(String name) { this.name = name; }
}

interface SpringTestUserRepository extends JpaRepository<SpringTestUser, Long> {
    List<SpringTestUser> findByName(String name);
}

@Service
class SpringTestUserService {
    private final SpringTestUserRepository userRepository;

    SpringTestUserService(SpringTestUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    SpringTestUser getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    SpringTestUser saveUser(SpringTestUser user) {
        return userRepository.save(user);
    }

    List<SpringTestUser> findByName(String name) {
        return userRepository.findByName(name);
    }

    SpringTestUser requireUser(Long id) {
        return userRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }
}

@RestController
@RequestMapping("/users")
class SpringTestUserController {
    private final SpringTestUserService userService;

    SpringTestUserController(SpringTestUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    ResponseEntity<SpringTestUser> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping
    ResponseEntity<SpringTestUser> createUser(@RequestBody SpringTestUser user) {
        return ResponseEntity.ok(userService.saveUser(user));
    }
}

@ControllerAdvice
class SpringTestGlobalExceptionHandler {
    @ExceptionHandler(NoSuchElementException.class)
    ResponseEntity<String> handleNotFound(NoSuchElementException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }
}

// Exercise 1: Basic Unit Test for a Service Method
class SpringTestCalculatorService {
    int add(int a, int b) {
        return a + b;
    }
}

class SpringTestCalculatorServiceTest {
    @Test
    void add_shouldReturnSum() {
        assertEquals(5, new SpringTestCalculatorService().add(2, 3));
    }
}

// Exercise 2: Mocking a Repository in a Service Test
@ExtendWith(MockitoExtension.class)
class SpringTestUserServiceTest {
    @Mock
    SpringTestUserRepository userRepository;

    @InjectMocks
    SpringTestUserService userService;

    @Test
    void getUserById_shouldReturnUserFromRepository() {
        SpringTestUser user = new SpringTestUser(1L, "Alice");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        SpringTestUser result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals("Alice", result.getName());
    }

    @Test
    void requireUser_shouldThrowWhenMissing() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> userService.requireUser(99L));
    }
}

// Exercise 3: Testing a REST Controller with MockMvc
@WebMvcTest(SpringTestUserController.class)
class SpringTestControllerMockMvcTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    SpringTestUserService userService;

    @Test
    void getUser_shouldReturnJsonPayload() throws Exception {
        when(userService.getUserById(1L)).thenReturn(new SpringTestUser(1L, "Alice"));

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Alice"));
    }
}

// Exercise 4: Integration Test with Spring Boot
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
class SpringTestIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    SpringTestUserService userService;

    @Test
    void integrationTest_shouldBootApplicationContext() throws Exception {
        when(userService.getUserById(1L)).thenReturn(new SpringTestUser(1L, "Alice"));

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alice"));
    }
}

// Exercise 5: Test Controller POST Endpoint
@WebMvcTest(SpringTestUserController.class)
class SpringTestPostEndpointTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    SpringTestUserService userService;

    @Test
    void createUser_shouldReturnCreatedJson() throws Exception {
        when(userService.saveUser(any(SpringTestUser.class))).thenAnswer(invocation -> invocation.getArgument(0));

        String json = "{\"id\": 7, \"name\": \"Bob\"}";
        mockMvc.perform(post("/users")
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.name").value("Bob"));
    }
}

// Exercise 6: Test Service Exception Handling
class SpringTestServiceExceptionHandlingTest {
    @Mock
    SpringTestUserRepository repo;

    @InjectMocks
    SpringTestUserService service;

    @Test
    void shouldHandleMissingUser() {
        when(repo.findById(10L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> service.requireUser(10L));
    }
}

// Exercise 7: Test Custom Repository Query
class SpringTestCustomQueryTest {
    @Mock
    SpringTestUserRepository repo;

    @InjectMocks
    SpringTestUserService service;

    @Test
    void findByName_shouldReturnMatchingUsers() {
        when(repo.findByName("Alice")).thenReturn(List.of(new SpringTestUser(1L, "Alice")));
        assertEquals(1, service.findByName("Alice").size());
    }
}

// Exercise 8: Test Controller Exception Handling
class SpringTestGlobalExceptionHandlerTest {
    @Test
    void shouldReturnNotFoundMessage() {
        SpringTestGlobalExceptionHandler handler = new SpringTestGlobalExceptionHandler();
        ResponseEntity<String> response = handler.handleNotFound(new NoSuchElementException("missing"));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found", response.getBody());
    }
}

// Exercise 9: Parameterized Test with JUnit
class SpringTestParameterizedTest {
    @ParameterizedTest
    @ValueSource(ints = { 1, 2, 3, 5, 8 })
    void shouldAcceptPositiveInputs(int value) {
        assertTrue(value > 0);
    }
}
