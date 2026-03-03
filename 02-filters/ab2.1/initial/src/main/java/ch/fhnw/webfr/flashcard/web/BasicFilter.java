package ch.fhnw.webfr.flashcard.web;

import java.io.IOException;
import java.util.logging.Logger;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;

@WebFilter(urlPatterns = { "/*" })
public class BasicFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request,
            ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {
        var r = (HttpServletRequest) request;
        Logger logger = Logger.getLogger(BasicFilter.class.getName());
        logger.info("Before request [uri=" + r.getRequestURI() + "]");
        chain.doFilter(request, response);
    }
}
