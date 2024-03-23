package nl.timtwiest.simplejavaapi.app.boundary.resource;

import nl.timtwiest.simplejavaapi.api.models.LivenessResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class LivenessImplTest {

    @InjectMocks
    private LivenessImpl underTest;

    @Test
    void should_test_liveness() {
        ResponseEntity<LivenessResponse> responseEntity = underTest.liveness();

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        LivenessResponse livenessResponse = responseEntity.getBody();
        assertNotNull(livenessResponse);
        assertEquals(HttpStatus.OK.toString().toUpperCase(), livenessResponse.getStatus());
        assertNotNull(livenessResponse.getTimestamp());
    }
}