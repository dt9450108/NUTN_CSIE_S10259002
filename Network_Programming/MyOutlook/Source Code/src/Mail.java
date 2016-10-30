import java.io.Serializable;
import java.util.Vector;

public class Mail implements Serializable {
	private static final long serialVersionUID = 100L;

	public static enum STATUS {
		NONE, SEND, RECEIVE, DRAFT, DELETE;
	}

	public static int Count = 0;

	private int id;
	private String sender;
	private Vector<String> receivers;
	private String subject;
	private String content;
	private String date;
	private STATUS status;

	public Mail() {
		this.id = Mail.Count++;
		this.sender = null;
		this.receivers = new Vector<String>();
		this.subject = null;
		this.content = null;
		this.date = null;
		this.status = Mail.STATUS.NONE;
	}

	public Mail(String sender, Vector<String> receivers, String subject, String content, String date, Mail.STATUS status) {
		this.id = Mail.Count++;
		this.sender = sender;
		this.receivers = receivers;
		this.subject = subject;
		this.content = content;
		this.date = date;
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public String getSender() {
		return sender;
	}

	public String getSubject() {
		return subject;
	}

	public Vector<String> getReceivers() {
		return receivers;
	}

	public String getReceiverStr() {
		String str = "";
		for (int i = 0; i < this.receivers.size(); i++) {
			str += this.receivers.get(i);
			if (i < this.receivers.size() - 1)
				str += ", ";
		}
		return str;
	}

	public String getContent() {
		return content;
	}

	public STATUS getStatus() {
		return status;
	}

	public String getDate() {
		return date;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setReceivers(Vector<String> receivers) {
		this.receivers = receivers;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setStatus(STATUS status) {
		this.status = status;
	}

	public void setDate(String date) {
		this.date = date;
	}
}