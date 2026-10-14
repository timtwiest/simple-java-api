package nl.timtwiest.simplejavaapi.app.control.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.random.RandomGenerator;

@Service
public class QuoteService {
    private static final List<String> QUOTES = List.of(
            "May your builds be green and your dependencies current.",
            "The API awakens when the database is ready.",
            "A clean build is the path to the light side.",
            "Trust the contract, but verify the response.",
            "In a JVM far, far away, every test still passed."
    );

    private final RandomGenerator randomGenerator;

    public QuoteService() {
        this(RandomGenerator.getDefault());
    }

    QuoteService(RandomGenerator randomGenerator) {
        this.randomGenerator = randomGenerator;
    }

    public String randomQuote() {
        return QUOTES.get(randomGenerator.nextInt(QUOTES.size()));
    }
}
