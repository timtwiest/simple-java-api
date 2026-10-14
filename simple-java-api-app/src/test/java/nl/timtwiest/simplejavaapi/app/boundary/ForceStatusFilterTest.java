package nl.timtwiest.simplejavaapi.app.boundary;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ForceStatusFilterTest {

    @Test
    void shouldKeepTheForceBalancedForEveryRequest() throws Exception {
        MockHttpServletResponse response = new MockHttpServletResponse();

        new ForceStatusFilter().doFilter(
                new MockHttpServletRequest(), response, new MockFilterChain());

        assertEquals("balanced", response.getHeader("X-Force-Status"));
    }
}
