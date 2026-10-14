package nl.timtwiest.simplejavaapi.app.control.service;

import org.junit.jupiter.api.Test;

import java.util.random.RandomGenerator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class QuoteServiceTest {

    @Test
    void shouldUseTheForceToSelectAGalacticQuote() {
        RandomGenerator randomGenerator = mock(RandomGenerator.class);
        when(randomGenerator.nextInt(5)).thenReturn(3);

        QuoteService underTest = new QuoteService(randomGenerator);

        assertEquals("Trust the contract, but verify the response.", underTest.randomQuote());
    }
}
