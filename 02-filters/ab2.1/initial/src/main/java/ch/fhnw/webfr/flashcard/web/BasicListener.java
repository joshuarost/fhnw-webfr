package ch.fhnw.webfr.flashcard.web;

import ch.fhnw.webfr.flashcard.persistence.QuestionnaireRepository;
import ch.fhnw.webfr.flashcard.util.QuestionnaireInitializer;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class BasicListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String mode = sce.getServletContext().getInitParameter("mode");
        if (mode.equals("test")) {
            sce.getServletContext().setAttribute(
                    "questionnaireRepository",
                    new QuestionnaireInitializer().initRepoWithTestData());
        } else {
            sce.getServletContext().setAttribute(
                    "questionnaireRepository",
                    new QuestionnaireRepository());
        }
    }
}
