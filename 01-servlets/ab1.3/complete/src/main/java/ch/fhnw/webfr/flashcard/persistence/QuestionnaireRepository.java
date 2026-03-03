package ch.fhnw.webfr.flashcard.persistence;

import java.util.ArrayList;
import java.util.List;

import ch.fhnw.webfr.flashcard.domain.Questionnaire;

public class QuestionnaireRepository {
	private List<Questionnaire> questionnaires = new ArrayList<Questionnaire>();
	
	public Long save(Questionnaire q) {
		Long id = Long.valueOf(questionnaires.size());
		q.setId(id);
		questionnaires.add(q);
		return id;
	}
	
	public List<Questionnaire> findAll() {
		return questionnaires;
	}
	
	public Questionnaire findById(Long id) {
		return questionnaires.get(id.intValue());
	}
	
	public void clear() {
		questionnaires = new ArrayList<Questionnaire>();
	}
}
