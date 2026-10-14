package nl.timtwiest.simplejavaapi.app.boundary.resource.v1;

import nl.timtwiest.simplejavaapi.api.QuoteApiController;
import nl.timtwiest.simplejavaapi.api.models.QuoteResponse;
import nl.timtwiest.simplejavaapi.app.control.service.QuoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QuoteImpl implements QuoteApiController {
    private final QuoteService quoteService;

    public QuoteImpl(QuoteService quoteService) {
        this.quoteService = quoteService;
    }

    @Override
    public ResponseEntity<QuoteResponse> randomQuote() {
        return ResponseEntity.ok(new QuoteResponse(quoteService.randomQuote()));
    }
}
