package ch.uzh.marugoto.core.data.entity.topic;

public class Mail extends Notification {

    private String subject;
    private String body;
    private boolean openOnReceive;

    public Mail() {
        super();
    }

    public Mail(String subject, String body, Page page, Character from) {
        super(page, from);
        this.subject = subject;
        this.body = body;
    }

    public Mail(String subject, String body, Page page, Character from, VirtualTime receiveTimer) {
        super(receiveTimer, page, from);
        this.subject = subject;
        this.body = body;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean isOpenOnReceive() {
        return openOnReceive;
    }

    public void setOpenOnReceive(boolean openOnReceive) {
        this.openOnReceive = openOnReceive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Mail mail = (Mail) o;
        return id.equals(mail.id);
    }
}