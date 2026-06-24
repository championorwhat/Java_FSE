import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

/**
 * Solutions for advanced Mockito exercises.
 */
interface MockitoAdvRepository {
    String getData();
}

class MockitoAdvService {
    private final MockitoAdvRepository repository;

    MockitoAdvService(MockitoAdvRepository repository) {
        this.repository = repository;
    }

    String processData() {
        return "Processed " + repository.getData();
    }
}

// Exercise 1: Mocking Databases and Repositories
class MockitoAdvRepositoryTest {
    @Test
    void shouldMockRepositoryForServiceLogic() {
        MockitoAdvRepository mockRepository = mock(MockitoAdvRepository.class);
        when(mockRepository.getData()).thenReturn("Mock Data");

        MockitoAdvService service = new MockitoAdvService(mockRepository);
        String result = service.processData();

        assertEquals("Processed Mock Data", result);
        verify(mockRepository).getData();
    }
}

// Exercise 2: Mocking External Services (RESTful APIs)
interface MockitoAdvRestClient {
    String getResponse();
}

class MockitoAdvApiService {
    private final MockitoAdvRestClient restClient;

    MockitoAdvApiService(MockitoAdvRestClient restClient) {
        this.restClient = restClient;
    }

    String fetchData() {
        return "Fetched " + restClient.getResponse();
    }
}

class MockitoAdvRestClientTest {
    @Test
    void shouldMockRestClient() {
        MockitoAdvRestClient mockRestClient = mock(MockitoAdvRestClient.class);
        when(mockRestClient.getResponse()).thenReturn("Mock Response");

        MockitoAdvApiService service = new MockitoAdvApiService(mockRestClient);
        String result = service.fetchData();

        assertEquals("Fetched Mock Response", result);
    }
}

// Exercise 3: Mocking File I/O
interface MockitoAdvFileReader {
    String read();
}

interface MockitoAdvFileWriter {
    void write(String content);
}

class MockitoAdvFileService {
    private final MockitoAdvFileReader reader;
    private final MockitoAdvFileWriter writer;

    MockitoAdvFileService(MockitoAdvFileReader reader, MockitoAdvFileWriter writer) {
        this.reader = reader;
        this.writer = writer;
    }

    String processFile() {
        String content = reader.read();
        writer.write(content);
        return "Processed " + content;
    }
}

class MockitoAdvFileIoTest {
    @Test
    void shouldMockFileReaderAndWriter() {
        MockitoAdvFileReader mockReader = mock(MockitoAdvFileReader.class);
        MockitoAdvFileWriter mockWriter = mock(MockitoAdvFileWriter.class);
        when(mockReader.read()).thenReturn("Mock File Content");

        MockitoAdvFileService service = new MockitoAdvFileService(mockReader, mockWriter);
        String result = service.processFile();

        assertEquals("Processed Mock File Content", result);
        verify(mockWriter).write("Mock File Content");
    }
}

// Exercise 4: Mocking Network Interactions
interface MockitoAdvNetworkClient {
    String connect();
}

class MockitoAdvNetworkService {
    private final MockitoAdvNetworkClient client;

    MockitoAdvNetworkService(MockitoAdvNetworkClient client) {
        this.client = client;
    }

    String connectToServer() {
        return "Connected to " + client.connect();
    }
}

class MockitoAdvNetworkTest {
    @Test
    void shouldMockNetworkClient() {
        MockitoAdvNetworkClient mockNetworkClient = mock(MockitoAdvNetworkClient.class);
        when(mockNetworkClient.connect()).thenReturn("Mock Connection");

        MockitoAdvNetworkService service = new MockitoAdvNetworkService(mockNetworkClient);
        String result = service.connectToServer();

        assertEquals("Connected to Mock Connection", result);
    }
}

// Exercise 5: Mocking Multiple Return Values
class MockitoAdvMultipleReturnValuesTest {
    @Test
    void shouldReturnDifferentValuesConsecutively() {
        MockitoAdvRepository mockRepository = mock(MockitoAdvRepository.class);
        when(mockRepository.getData()).thenReturn("First Mock Data", "Second Mock Data");

        MockitoAdvService service = new MockitoAdvService(mockRepository);

        assertEquals("Processed First Mock Data", service.processData());
        assertEquals("Processed Second Mock Data", service.processData());
    }
}
