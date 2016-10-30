import java.io.Serializable;

public class Book implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String author;
	private User user;
	private boolean state;

	public Book(String name, String author) {
		this.name = name;
		this.author = author;
		this.user = null;
		this.state = false;
	}

	public String toString() {
		return String.format("Book name: %s, Author: %s , Borrower: %s\n", this.name, this.author, (this.state ? this.user.getName() : "None"));
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		Book t = (Book) obj;
		return (this.name.equals(t.getName()) && this.author.equals(t.getAuthor()));
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setState(boolean state) {
		this.state = state;
	}

	public String getName() {
		return this.name;
	}

	public String getAuthor() {
		return this.author;
	}

	public User getUser() {
		return this.user;
	}

	public boolean getState() {
		return this.state;
	}
}
