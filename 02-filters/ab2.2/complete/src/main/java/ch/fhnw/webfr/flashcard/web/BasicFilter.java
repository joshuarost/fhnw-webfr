package ch.fhnw.webfr.flashcard.web;

import java.io.IOException;
import java.util.logging.Logger;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;

@WebFilter(urlPatterns={"/*"})
public class BasicFilter implements Filter {
	public static final String DEFAULT_BEFORE_MESSAGE_PREFIX = "Before request [";
	public static final String DEFAULT_BEFORE_MESSAGE_SUFFIX = "]";
	private Logger logger = Logger.getLogger(BasicFilter.class.getName());
	
	public void init(FilterConfig filterConfig) throws ServletException {
		logger.info("init() called");
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		StringBuffer buffer = new StringBuffer();
		buffer.append(DEFAULT_BEFORE_MESSAGE_PREFIX);
		buffer.append("uri=").append(httpRequest.getRequestURI());
		buffer.append(DEFAULT_BEFORE_MESSAGE_SUFFIX);
		logger.info(buffer.toString());
		chain.doFilter(request, response);
	}

	public void destroy() {
		logger.info("destroy() called");
	}

}
