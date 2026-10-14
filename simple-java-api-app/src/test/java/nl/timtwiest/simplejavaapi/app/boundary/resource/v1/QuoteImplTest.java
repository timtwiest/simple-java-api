package nl.timtwiest.simplejavaapi.app.boundary.resource.v1;

import nl.timtwiest.simplejavaapi.api.models.QuoteResponse;
import nl.timtwiest.simplejavaapi.app.control.service.QuoteService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuoteImplTest {
    @Mock
    private QuoteService quoteService;

    @InjectMocks
    private QuoteImpl underTest;

    @Test
    void shouldReturnWisdomFromAJvmFarFarAway() {
        when(quoteService.randomQuote()).thenReturn("A clean build is the path to the light side.");

        ResponseEntity<QuoteResponse> response = underTest.randomQuote();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("A clean build is the path to the light side.", response.getBody().getQuote());
    }
}
