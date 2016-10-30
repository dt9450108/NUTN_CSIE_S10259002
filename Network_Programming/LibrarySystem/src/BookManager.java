import java.util.LinkedList;

public class BookManager {
	private LinkedList<Book> books;

	public BookManager() {
		this.books = new LinkedList<Book>();
	}

	public Book getBook(String name) {
		for (int i = 0; i < books.size(); i++)
			if (books.get(i).getName().equals(name))
				return books.get(i);
		return null;
	}

	public LinkedList<Book> getBooks() {
		return this.books;
	}

	public void setBooks(LinkedList<Book> books) {
		this.books = books;
	}

	public boolean add(String name, String author) {
		Book t = getBook(name);
		if (t == null) {
			this.books.add(new Book(name, author));
			return true;
		}
		return false;
	}

	public boolean edit(String name, String author, boolean state, User user) {
		Book t = getBook(name);
		if (t != null) {
			if (!t.getName().equals(name))
				t.setName(name);
			if (!t.getAuthor().equals(author))
				t.setAuthor(author);
			if (t.getState() != state)
				t.setState(state);
			if (user != null && !t.getUser().equals(user))
				t.setUser(user);
			return true;
		}
		return false;
	}

	public boolean del(String name) {
		Book t = getBook(name);
		if (t != null) {
			this.books.remove(t);
			return true;
		}
		return false;
	}

	public void query(String name) {
		Book t = getBook(name);
		if (t != null) {
			System.out.print(t.toString());
		} else {
			System.out.printf("There is no such book \"%s\"\n", name);
		}
	}

	public String lquery(String name) {
		return "next version\n";
	}

	public void dumpBooks() {
		for (int i = 0; i < books.size(); i++) {
			System.out.printf("\t%d.\t%s", (i + 1), books.get(i).toString());
		}
	}
}
