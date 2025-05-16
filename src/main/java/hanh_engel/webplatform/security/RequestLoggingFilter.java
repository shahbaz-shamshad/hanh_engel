package hanh_engel.webplatform.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestLoggingFilter implements Filter {

    private static final Logger logger = LogManager.getLogger(RequestLoggingFilter.class);

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        logger.info("\n=== Incoming Request ===");
        logger.info("Method: {}", request.getMethod());
        logger.info("URI: {}", request.getRequestURI());
        logger.info("Auth Header: {}", request.getHeader("Authorization"));

        chain.doFilter(req, res);

        HttpServletResponse response = (HttpServletResponse) res;
        logger.info("Response Status: {}", response.getStatus());
        logger.info("========================");
    }
}