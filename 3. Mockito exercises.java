import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

/**
 * Solutions for the basic Mockito hands-on exercises.
 */
interface MockitoBasicExternalApi {
    String getData();

    String getDataById(long id);

    void save(String payload);
}

class MockitoBasicMyService {
    private final MockitoBasicExternalApi api;

    MockitoBasicMyService(MockitoBasicExternalApi api) {
        this.api = api;
    }

    String fetchData() {
        return api.getData();
    }

    String fetchDataWithId(long id) {
        return api.getDataById(id);
    }

    void store(String payload) {
        api.save(payload);
    }
}

// Exercise 1: Mocking and Stubbing
class MockitoBasicMockingTest {
    @Test
    void shouldMockAndStubExternalApi() {
        MockitoBasicExternalApi mockApi = mock(MockitoBasicExternalApi.class);
        when(mockApi.getData()).thenReturn("Mock Data");

        MockitoBasicMyService service = new MockitoBasicMyService(mockApi);
        String result = service.fetchData();

        assertEquals("Mock Data", result);
    }
}

// Exercise 2: Verifying Interactions
class MockitoBasicVerificationTest {
    @Test
    void shouldVerifyInteraction() {
        MockitoBasicExternalApi mockApi = mock(MockitoBasicExternalApi.class);
        when(mockApi.getData()).thenReturn("Mock Data");

        MockitoBasicMyService service = new MockitoBasicMyService(mockApi);
        service.fetchData();

        verify(mockApi).getData();
        verifyNoMoreInteractions(mockApi);
    }
}

// Exercise 3: Argument Matching
class MockitoBasicArgumentMatchingTest {
    @Test
    void shouldVerifyArgumentsUsingMatchers() {
        MockitoBasicExternalApi mockApi = mock(MockitoBasicExternalApi.class);
        when(mockApi.getDataById(anyLong())).thenReturn("User-42");

        MockitoBasicMyService service = new MockitoBasicMyService(mockApi);
        String result = service.fetchDataWithId(42L);

        assertEquals("User-42", result);
        verify(mockApi).getDataById(eq(42L));
    }
}

// Exercise 4: Handling Void Methods
class MockitoBasicVoidMethodTest {
    @Test
    void shouldStubAndVerifyVoidMethod() {
        MockitoBasicExternalApi mockApi = mock(MockitoBasicExternalApi.class);

        doNothing().when(mockApi).save("payload-1");

        MockitoBasicMyService service = new MockitoBasicMyService(mockApi);
        service.store("payload-1");

        verify(mockApi).save("payload-1");
    }
}

// Exercise 5: Mocking and Stubbing with Multiple Returns
class MockitoBasicMultipleReturnsTest {
    @Test
    void shouldReturnDifferentValuesOnConsecutiveCalls() {
        MockitoBasicExternalApi mockApi = mock(MockitoBasicExternalApi.class);
        when(mockApi.getData()).thenReturn("First", "Second");

        MockitoBasicMyService service = new MockitoBasicMyService(mockApi);

        assertEquals("First", service.fetchData());
        assertEquals("Second", service.fetchData());
    }
}

// Exercise 6: Verifying Interaction Order
class MockitoBasicOrderVerificationTest {
    @Test
    void shouldVerifyCallOrder() {
        MockitoBasicExternalApi mockApi = mock(MockitoBasicExternalApi.class);
        MockitoBasicMyService service = new MockitoBasicMyService(mockApi);

        service.store("one");
        service.fetchData();

        InOrder order = inOrder(mockApi);
        order.verify(mockApi).save("one");
        order.verify(mockApi).getData();
    }
}

// Exercise 7: Handling Void Methods with Exceptions
class MockitoBasicVoidExceptionTest {
    @Test
    void shouldThrowWhenVoidMethodIsStubbedToFail() {
        MockitoBasicExternalApi mockApi = mock(MockitoBasicExternalApi.class);
        doThrow(new RuntimeException("Save failed")).when(mockApi).save("bad");

        MockitoBasicMyService service = new MockitoBasicMyService(mockApi);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.store("bad"));
        assertEquals("Save failed", ex.getMessage());
    }
}
