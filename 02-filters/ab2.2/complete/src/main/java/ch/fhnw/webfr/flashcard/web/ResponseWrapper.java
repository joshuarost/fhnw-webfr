package ch.fhnw.webfr.flashcard.web;

import java.io.CharArrayReader;
import java.io.CharArrayWriter;
import java.io.PrintWriter;
import java.io.Reader;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;


public class ResponseWrapper extends HttpServletResponseWrapper {

	public ResponseWrapper(HttpServletResponse response){
		super(response);
	}

	private CharArrayWriter wr = new CharArrayWriter();
	
	public PrintWriter getWriter(){
		return new PrintWriter(wr);
	}
	
	public Reader getReader() {
		wr.flush();
		wr.close();
		return new CharArrayReader(wr.toCharArray());
	}
}