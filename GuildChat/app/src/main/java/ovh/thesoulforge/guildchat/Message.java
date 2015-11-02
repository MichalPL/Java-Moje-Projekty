package ovh.thesoulforge.guildchat;

/**
 * Created by Michal on 2015-08-13.
 */
public class Message {
    private String fromName, message;
    private int isSelf;
    private int czy;

    public Message() {
    }

    public Message(String fromName, String message, int isSelf) {
        this.fromName = fromName;
        this.message = message;
        this.isSelf = isSelf;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int isSelf() {
        return isSelf;
    }

    public void setSelf(int isSelf) {
        this.isSelf = isSelf;
    }

}