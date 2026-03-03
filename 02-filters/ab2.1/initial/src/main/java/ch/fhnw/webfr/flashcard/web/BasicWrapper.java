package ch.fhnw.webfr.flashcard.web;

import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

@WebFilter(urlPatterns = { "/*" })
public class BasicWrapper extends HttpServletResponseWrapper {

    public BasicWrapper(HttpServletResponse response) {
        super(response);
    }

}