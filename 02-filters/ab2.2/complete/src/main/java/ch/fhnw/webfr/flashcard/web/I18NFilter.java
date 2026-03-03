package ch.fhnw.webfr.flashcard.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Map.Entry;
import java.util.Properties;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;


@WebFilter(urlPatterns={"/*"})
public class I18NFilter implements Filter {
	private static String DEFAULT_FILENAME = "messages.properties";
	private Properties i18nStrings;

	public void init(FilterConfig filterConfig) throws ServletException {
		// Get filename from servlet context
		String filename = (String) filterConfig.getServletContext().getInitParameter("i18n");
		if (filename == null) {
			filename = DEFAULT_FILENAME;
		}
		// Load i18n messages from properties file
		i18nStrings = new Properties();
		try {
			InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename);
			i18nStrings.load(new InputStreamReader(input, Charset.forName("UTF-8")));
		}
		catch (IOException e) {
			throw new ServletException(e.getMessage());
		}
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		// We are on the INBOUND path!
		
		// Initialize a ResponseWrapper to put into the chain, wrapping the original response
		ResponseWrapper rw = new ResponseWrapper((HttpServletResponse) response);
		
		// Put the ResponseWrapper into the chain
		chain.doFilter(request, rw);

		// We are on the OUTBOUND path!
		
		// Read the actual response which should be checked and if necessary updated.
		BufferedReader rd = new BufferedReader(rw.getReader());
		String line = rd.readLine();
		StringBuffer sb = new StringBuffer();
		// Loop through each line of the response
		while (line != null) {
			// Check if a translation is necessary
			for (Entry<Object, Object> e : i18nStrings.entrySet()) {
				line = line.replace((String) e.getKey(), (String) e.getValue());
			}
			sb.append(line);
			line = rd.readLine();
		}
		
		// Write updated response into original response instance
		Writer wr = response.getWriter();
		wr.append(sb.toString());
		wr.flush();		
	}

	public void destroy() {
		// do nothing
	}

}
