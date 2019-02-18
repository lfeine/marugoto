package ch.uzh.marugoto.core.data.entity.topic;

import com.arangodb.springframework.annotation.Ref;

public class Criteria {
    private PageCriteriaType pageCriteria;
    private ExerciseCriteriaType exerciseCriteria;
    private MailCriteriaType mailCriteriaType;
    @Ref
    private Exercise affectedExercise;
    @Ref
    private Page affectedPage;
    @Ref
    private Mail affectedMail;

    public Criteria() {
        super();
    }

    public Criteria(PageCriteriaType pageCriteria, Page affectedPage) {
        this();
        this.pageCriteria = pageCriteria;
        this.affectedPage = affectedPage;
    }

    public Criteria(ExerciseCriteriaType exerciseCriteria, Exercise affectedExercise) {
        this();
        this.exerciseCriteria = exerciseCriteria;
        this.affectedExercise = affectedExercise;
    }

    public Criteria(Mail mail, MailCriteriaType mailCriteriaType) {
        this();
        this.mailCriteriaType = mailCriteriaType;
        this.affectedMail = mail;
    }

    public Criteria(PageCriteriaType pageCriteria, Page affectedPage, ExerciseCriteriaType exerciseCriteria, Exercise affectedExercise) {
        this();
        this.pageCriteria = pageCriteria;
        this.affectedPage = affectedPage;
        this.exerciseCriteria = exerciseCriteria;
        this.affectedExercise = affectedExercise;
    }
    
    public void setPageCriteria(PageCriteriaType pageCriteria) {
		this.pageCriteria = pageCriteria;
	}

	public PageCriteriaType getPageCriteria() {
        return pageCriteria;
    }
	
	public void setExerciseCriteria(ExerciseCriteriaType exerciseCriteria) {
		this.exerciseCriteria = exerciseCriteria;
	}

    public ExerciseCriteriaType getExerciseCriteria() {
        return exerciseCriteria;
    }

    public MailCriteriaType getMailCriteriaType() {
        return mailCriteriaType;
    }

    public void setMailCriteriaType(MailCriteriaType mailCriteriaType) {
        this.mailCriteriaType = mailCriteriaType;
    }

    public void setAffectedExercise(Exercise affectedExercise) {
		this.affectedExercise = affectedExercise;
	}

	public Exercise getAffectedExercise() {
        return affectedExercise;
    }

	public void setAffectedPage(Page affectedPage) {
		this.affectedPage = affectedPage;
	}
	
    public Page getAffectedPage() {
        return affectedPage;
    }

    public Mail getAffectedMail() {
        return affectedMail;
    }

    public void setAffectedMail(Mail affectedMail) {
        this.affectedMail = affectedMail;
    }

    public boolean isForPage() {
        return this.affectedPage != null;
    }

    public boolean isForExercise() {
        return this.affectedExercise != null;
    }

    public boolean isForMail() {
        return this.affectedMail != null;
    }
}