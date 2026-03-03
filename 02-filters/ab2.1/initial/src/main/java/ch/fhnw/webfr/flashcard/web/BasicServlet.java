package ch.fhnw.webfr.flashcard.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import ch.fhnw.webfr.flashcard.domain.Questionnaire;
import ch.fhnw.webfr.flashcard.persistence.QuestionnaireRepository;
import ch.fhnw.webfr.flashcard.util.QuestionnaireInitializer;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// import jakarta.servlet.annotation.WebServlet;

// While using @WebServlet, you can delete file 'web.xml'
@WebServlet(urlPatterns = { "/*" })
public class BasicServlet extends HttpServlet {
	/*
	 * Attention: This repository will be used by all clients, concurrency could be
	 * a problem. THIS VERSION IS NOT PRODUCTION READY!
	 */
	private QuestionnaireRepository questionnaireRepository;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html; charset=utf-8");

		String[] pathElements = request.getRequestURI().split("/");
		if (isLastPathElementQuestionnaireId(pathElements)) {
			Long id = Long.valueOf(pathElements[pathElements.length - 1]);
			handleQuestionnaireIdRequest(request, response, id);
		} else if (isLastPathElementQuestionnaires(pathElements)) {
			handleQuestionnairesRequest(request, response);
		} else {
			handleIndexRequest(request, response);
		}
	}

	private boolean isLastPathElementQuestionnaires(String[] pathElements) {
		String last = pathElements[pathElements.length - 1];
		return last.equals("questionnaires");
	}

	private boolean isLastPathElementQuestionnaireId(String[] pathElements) {
		String id = pathElements[pathElements.length - 1];
		try {
			// Check if last pathelement is a numeric value
			Long.parseLong(id);
		} catch (NumberFormatException | NullPointerException nfe) {
			return false;
		}
		return true;
	}

	private void handleQuestionnaireIdRequest(HttpServletRequest request, HttpServletResponse response, Long id)
			throws IOException {
		Questionnaire questionnaire;
		PrintWriter writer = response.getWriter();
		try {
			questionnaire = questionnaireRepository.findById(id);
		} catch (Exception e) {
			questionnaire = null;
		}
		writer.append("<html><head><title>Example</title></head><body>");
		writer.append("<h3>Questionnaire</h3>");
		if (questionnaire != null) {
			writer.append("<strong>").append(questionnaire.getTitle()).append("</strong></br>");
			writer.append("<span>").append(questionnaire.getDescription()).append("</span></p>");
		} else {
			writer.append("no questionnaire found");
		}
		writer.append("</body></html>");
	}

	private void handleQuestionnairesRequest(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		List<Questionnaire> questionnaires = questionnaireRepository.findAll();
		PrintWriter writer = response.getWriter();
		writer.append("<html><head><title>Example</title></head><body>");
		writer.append("<h3>Fragebögen</h3>");
		for (Questionnaire questionnaire : questionnaires) {
			String url = request.getContextPath() + request.getServletPath();
			url = url + "/questionnaires/" + questionnaire.getId().toString();
			writer.append("<p><a href='").append(response.encodeURL(url)).append("'>").append(questionnaire.getTitle())
					.append("</a></p>");
		}
		writer.append("</body></html>");
	}

	private void handleIndexRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
		PrintWriter writer = response.getWriter();
		String url = request.getContextPath() + request.getServletPath();
		writer.append("<html><head><title>Example</title></head><body><h3>Welcome</h3>");
		writer.append("<p><a href='").append(response.encodeURL(url))
				.append("/questionnaires'>All Questionnaires</a></p></body></html>");
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		questionnaireRepository = new QuestionnaireInitializer().initRepoWithTestData();
	}

}
