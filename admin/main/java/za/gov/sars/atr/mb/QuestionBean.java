/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.gov.sars.atr.mb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.springframework.beans.factory.annotation.Autowired;
import za.gov.sars.common.QuestionType;
import za.gov.sars.domain.Question;
import za.gov.sars.service.QuestionServiceLocal;

/**
 *
 * @author S2030707
 */
@ManagedBean
@ViewScoped
public class QuestionBean extends BaseBean<Question> {

    @Autowired
    private QuestionServiceLocal questionService;
    private List<QuestionType> questionTypes = new ArrayList();

    @PostConstruct
    public void init() {
        reset().setList(true);
        questionTypes.addAll(Arrays.asList(QuestionType.values()));
        addCollections(questionService.findAll());
    }

    public void addorUpdate(Question question) {
        reset().setAdd(true);
        if (question != null) {
            question.setUpdatedBy(getActiveUser().getSid());
            question.setUpdatedDate(new Date());

        } else {
            question = new Question();
            question.setCreatedBy(getActiveUser().getSid());
            question.setCreatedDate(new Date());
            addCollection(question);
        }
        addEntity(question);
    }

    public void save(Question question) {
        if (question.getId()!= null) {
            questionService.update(question);
            addInformationMessage("Record has been updated successfully");
        } else {
            questionService.save(question);
            addInformationMessage("Record has been successfully saved");
        }
        reset().setList(true);
    }

    public void delete(Question question) {
        questionService.deleteById(question.getId());
        remove(question);
        addInformationMessage("Recored has been successfuly deleted");
        reset().setList(true);
    }

    public void cancel(Question question) {
        if (question.getId() == null && getQuestions().contains(question)) {
            remove(question);
        }
        reset().setList(true);
    }

    public List<Question> getQuestions() {
        return this.getCollections();
    }

    public List<QuestionType> getQuestionTypes() {
        return questionTypes;
    }

    public void setQuestionTypes(List<QuestionType> questionTypes) {
        this.questionTypes = questionTypes;
    }

}
