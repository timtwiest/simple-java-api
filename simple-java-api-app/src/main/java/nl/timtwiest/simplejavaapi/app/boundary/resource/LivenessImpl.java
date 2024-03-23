package nl.timtwiest.simplejavaapi.app.boundary.resource;

import lombok.AllArgsConstructor;
import nl.timtwiest.simplejavaapi.api.LivenessApiController;
import nl.timtwiest.simplejavaapi.api.models.LivenessResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for liveness checks.
 * This class provides endpoints for checking the health and liveness of the application.
 */
@RestController
@AllArgsConstructor
public class LivenessImpl implements LivenessApiController {

    /**
     * Checks the liveness of the application.
     * Returns a response entity containing the HTTP status and current timestamp.
     *
     * @return ResponseEntity containing the liveness status.
     */
    @Override
    public ResponseEntity<LivenessResponse> liveness() {
        return ResponseEntity.ok(LivenessResponse.builder()
                .status(HttpStatus.OK.toString())
                .timestamp(System.currentTimeMillis())
                .build());
    }
}

