package NotepadPackage;

import java.io.Serializable;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**Класс, описывающйи объект Заметка*/
public class Note implements Serializable {
    private String topic;
    private Date date;
    private String email;
    private String text;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "topic: "+topic+"; date: "+date+"; email: "+email+"; text: "+text;
    }

    /**Если поля вызывающего объекта состоят из полей передаваемого объекта note,
     * то вернется  true, иначе false. Используется для поиска по ключевым словам*/
    public boolean consistsOf(Note note) {
        if (note==null)
            return false;
        Boolean bTopic;
        Boolean bText;
        Boolean bEmail;
        Pattern pattern=Pattern.compile(note.topic.toLowerCase());
        Matcher matcher=pattern.matcher(this.topic.toLowerCase());
        bTopic=matcher.find();
        pattern=Pattern.compile(note.email.toLowerCase());
        matcher=pattern.matcher(this.email.toLowerCase());
        bEmail=matcher.find();
        pattern=Pattern.compile(note.text.toLowerCase());
        matcher=pattern.matcher(this.text.toLowerCase());
        bText=matcher.find();
        if (bEmail && bText && bTopic)
            return true;
        else
            return false;
    }


    public static class NoteBuilder {
        Note note;

        public NoteBuilder() {
            note=new Note();
        }

        public NoteBuilder withTopic(String topic) {
            note.topic=topic;
            return this;
        }

        public NoteBuilder withDate(Date date) {
            note.date=date;
            return this;
        }

        public NoteBuilder withEmail(String email) {
            note.email=email;
            return this;
        }

        public NoteBuilder withText(String text) {
            note.text=text;
            return this;
        }

        public Note Build() {
            return note;
        }
    }
}
