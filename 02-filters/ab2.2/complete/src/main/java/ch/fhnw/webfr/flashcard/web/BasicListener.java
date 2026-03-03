package ch.fhnw.webfr.flashcard.web;

import java.util.logging.Logger;

import ch.fhnw.webfr.flashcard.persistence.QuestionnaireRepository;
import ch.fhnw.webfr.flashcard.util.QuestionnaireInitializer;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class BasicListener implements ServletContextListener {
	private Logger logger = Logger.getLogger(BasicListener.class.getName());

	public void contextInitialized(ServletContextEvent sce) {
		String mode = (String) sce.getServletContext().getInitParameter("mode");
		QuestionnaireRepository repo = null;
		if ((mode != null) && (mode.equals("test"))) {
			repo = new QuestionnaireInitializer().initRepoWithTestData();
		} else {
			repo = new QuestionnaireRepository();
		}
		sce.getServletContext().setAttribute("questionnaireRepository", repo);
		logger.info("mode is " + mode);
	}

	public void contextDestroyed(ServletContextEvent sce) {
		// not used here
	}

}
