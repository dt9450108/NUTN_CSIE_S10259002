import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.Scanner;

public class LibMS {
	private String booksfn;
	private String usersfn;
	private BookManager lib;
	private UserManager usr;

	public static void main(String[] args) {
		LibMS libms = new LibMS("books.dat", "users.dat");
		PrintStream jout = System.out;
		Scanner jin = new Scanner(System.in);
		boolean exit = false;
		int cmd = -1;

		libms.LibInitialize();

		jout.println("\tThis is a library manage system!\n");
		libms.help();
		while (!exit) {
			jout.println("\nEnter the action code to do what you need: ");
			while (!jin.hasNextInt()) {
				jout.println("\tThat's not a number!");
				jin.next();

			}
			cmd = jin.nextInt();
			jin.nextLine();

			switch (cmd) {
				case 0:
					// print help information
					libms.help();
					break;
				case 1: {
					// add user
					String name, phone, email;
					jout.println("==================== add user");
					jout.println("Enter your name: ");
					name = jin.nextLine();
					jout.println("Enter your phone number: ");
					phone = jin.next();
					jout.println("Enter your email: ");
					email = jin.next();

					UserManager um = libms.getUsr();
					if (um.add(name, phone, email)) {
						jout.println("\tSuccess to register!");
					} else {
						jout.println("\tUser \"" + name + "\" is existed!");
					}
					break;
				}
				case 2: {
					// edit user
					String name, phone, email;
					jout.println("==================== edit user");
					jout.println("Enter your name: ");
					name = jin.nextLine();
					jout.println("Enter your phone number: ");
					phone = jin.next();
					jout.println("Enter your email: ");
					email = jin.next();

					UserManager um = libms.getUsr();
					if (um.edit(name, phone, email)) {
						jout.println("\tSuccess to edit!");
					} else {
						jout.println("\tThere is no the user with name: \"" + name + "\".");
					}
					break;
				}
				case 3: {
					// delete user
					String name;
					jout.println("==================== delete user");
					jout.println("Enter your name: ");
					name = jin.nextLine();

					UserManager um = libms.getUsr();
					if (um.del(name)) {
						jout.println("\tSuccess to delete!");
					} else {
						jout.println("\tThere is no the user with name: \"" + name + "\".");
					}
					break;
				}
				case 4: {
					// dump all users
					LinkedList<User> users = libms.getUsr().getUsers();
					jout.println("==================== dump all users");
					libms.getUsr().dumpUsers();
					break;
				}
				case 5: {
					// add book
					String name, author;
					jout.println("==================== add book");
					jout.println("Enter book name: ");
					name = jin.nextLine();
					jout.println("Enter book author: ");
					author = jin.nextLine();

					BookManager bm = libms.getLib();
					if (bm.add(name, author)) {
						jout.println("\tSuccess to add!");
					} else {
						jout.println("\tBook \"" + name + "\" is existed!");
					}
					break;
				}
				case 6: {
					// edit book
					String name, author;
					jout.println("==================== edit book");
					name = libms.lookBook();
					jout.println("Enter book author: ");
					author = jin.nextLine();

					BookManager bm = libms.getLib();
					Book b = bm.getBook(name);
					if (b != null) {
						if (bm.edit(name, author, b.getState(), b.getUser())) {
							jout.println("\tSuccess to edit!");
						} else {
							jout.println("\tThere is no the book with name: \"" + name + "\".");
						}
					}
					break;
				}
				case 7: {
					// delete book
					String name;
					jout.println("==================== delete book");
					name = libms.lookBook();
					BookManager bm = libms.getLib();
					if (bm.del(name)) {
						jout.println("\tSuccess to delete!");
					} else {
						jout.println("\tThere is no the book with name: \"" + name + "\".");
					}
					break;
				}
				case 8: {
					// dump all books
					LinkedList<Book> bl = libms.getLib().getBooks();
					jout.println("==================== dump all books");
					libms.getLib().dumpBooks();
					break;
				}
				case 9: {
					// lend book
					String book, user;
					jout.println("==================== lend book");
					jout.println("Enter you name: ");
					user = jin.nextLine();
					book = libms.lookBook();
					libms.lendBook(book, user);
					break;
				}
				case 10: {
					// return book
					String book;
					jout.println("==================== return book");
					book = libms.lookBook();
					libms.returnBook(book);
					break;
				}
				case 11: {
					// query book
					String name;
					jout.println("==================== query book");
					name = libms.lookBook();
					libms.qbook(name);
					break;
				}
				case 12: {
					// query user
					String name;
					jout.println("==================== query user");
					jout.println("Enter the user name: ");
					name = jin.nextLine();
					libms.quser(name);
					break;
				}
				case 14: {
					jout.println("==================== saving books and users data");
					jout.println("Success to save...\n");
					libms.LibFinalize();
					break;
				}
				case 15:
					// exit:
					jout.println("==================== exit system");
					exit = true;
					break;
				default:
					jout.println("==================== no action code");
					jout.println("\tThere is no such a action with code number : " + cmd + "\n");
			}
		}
		jin.close();
		jout.close();
		libms.LibFinalize();
	}

	public LibMS(String booksfn, String usersfn) {
		this.booksfn = booksfn;
		this.usersfn = usersfn;
		this.lib = new BookManager();
		this.usr = new UserManager();
	}

	public void LibInitialize() {
		// load books file
		try {
			File fBooks = new File(this.booksfn);
			if (fBooks.exists()) {
				FileInputStream fisBooks = new FileInputStream(this.booksfn);
				ObjectInputStream oisBooks = new ObjectInputStream(fisBooks);
				this.lib.setBooks((LinkedList<Book>) oisBooks.readObject());
				oisBooks.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// load users file
		try {
			File fUsers = new File(this.usersfn);
			if (fUsers.exists()) {
				FileInputStream fisUsers = new FileInputStream(this.usersfn);
				ObjectInputStream oisUsers = new ObjectInputStream(fisUsers);
				this.usr.setUsers((LinkedList<User>) oisUsers.readObject());
				oisUsers.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void LibFinalize() {
		// store books file
		try {
			FileOutputStream fosBooks = new FileOutputStream(this.booksfn, false);
			ObjectOutputStream oosBooks = new ObjectOutputStream(fosBooks);
			oosBooks.writeObject(this.lib.getBooks());
			oosBooks.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// store users file
		try {
			FileOutputStream fosUsers = new FileOutputStream(this.usersfn, false);
			ObjectOutputStream oosUsers = new ObjectOutputStream(fosUsers);
			oosUsers.writeObject(this.usr.getUsers());
			oosUsers.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void qbook(String name) {
		Book t = lib.getBook(name);
		if (t != null)
			System.out.print("\t" + t.toString() + "\n");
		else
			System.out.print("\tNo such book!\n");
	}

	public void quser(String name) {
		User u = usr.getUser(name);

		if (u != null) {
			boolean noborrowed = true;
			System.out.print("\t" + u.toString());
			LinkedList<Book> ts = lib.getBooks();
			for (int i = 0; i < ts.size(); i++) {
				Book t = ts.get(i);
				if (t.getState() && t.getUser().equals(u)) {
					if (noborrowed)
						noborrowed = false;
					System.out.printf("\t%d.\t%s", (i + 1), t.toString());
				}
			}

			if (noborrowed) {
				System.out.printf("\tUser: \"%s\" no borrow any book.\n", name);
			}
		}
	}

	public void lendBook(String book, String user) {
		User u = usr.getUser(user);
		if (u == null) {
			System.out.println("\tYou are not registered, please register first!\n");
			return;
		}

		Book t = lib.getBook(book);
		if (t != null) {
			if (t.getState()) {
				System.out.printf("\tThe book \"%s\" is borrower by \"%s\".\n", book, t.getUser().getName());
			} else {
				t.setState(true);
				t.setUser(u);
				System.out.println("\tSuccess to borrow the book \"" + book + "\".");
			}
		} else {
			System.out.printf("\tThere is no the book \"%s\".\n", book);
		}
	}

	public void returnBook(String book) {
		Book t = lib.getBook(book);
		if (t != null) {
			if (t.getState()) {
				t.setUser(null);
				t.setState(false);
				System.out.println("\tSuccess to return the book \"" + book + "\".");
			} else {
				System.out.printf("\tThe book \"%s\" had been borrowed.\n", book);
			}
		} else {
			System.out.printf("\tThere is no the book \"%s\"\n", book);
		}
	}

	public BookManager getLib() {
		return this.lib;
	}

	public UserManager getUsr() {
		return this.usr;
	}

	public String lookBook() {
		LinkedList<Book> books = this.lib.getBooks();
		LinkedList<String> maybe = new LinkedList<String>();
		Scanner jin = new Scanner(System.in);
		String target = null;
		boolean found = false;

		for (int j = 0; j < 2 && !found; j++) {
			System.out.println("Enter the book name: ");
			target = jin.nextLine();

			for (int i = 0; i < books.size(); i++) {
				String tmp = books.get(i).getName();
				if (tmp.equals(target)) {
					found = true;
					break;
				} else {
					if (keyWordSearch(tmp, target))
						maybe.add(tmp);
				}
			}

			if (!found && maybe.size() > 0) {
				System.out.printf("Hint: ");
				for (int i = 0; i < maybe.size(); i++) {
					System.out.printf("\"%s\"", maybe.get(i));
					if (i < maybe.size() - 1)
						System.out.print(", ");
				}
				System.out.println();
			} else {
				if (!found)
					System.out.println("No probable book!");
			}
			maybe.clear();
		}

		if (found)
			return target;
		else
			return "";
	}

	private boolean keyWordSearch(String str1, String str2) {
		String word1 = str1.toLowerCase();
		String word2 = str2.toLowerCase();
		int len1 = word1.length();
		int len2 = word2.length();

		// len1+1, len2+1, because finally return dp[len1][len2]
		int[][] dp = new int[len1 + 1][len2 + 1];

		for (int i = 0; i <= len1; i++) {
			dp[i][0] = i;
		}

		for (int j = 0; j <= len2; j++) {
			dp[0][j] = j;
		}

		//iterate though, and check last char
		for (int i = 0; i < len1; i++) {
			char c1 = word1.charAt(i);
			for (int j = 0; j < len2; j++) {
				char c2 = word2.charAt(j);

				//if last two chars equal
				if (c1 == c2) {
					//update dp value for +1 length
					dp[i + 1][j + 1] = dp[i][j];
				} else {
					int replace = dp[i][j] + 1;
					int insert = dp[i][j + 1] + 1;
					int delete = dp[i + 1][j] + 1;

					int min = replace > insert ? insert : replace;
					min = delete > min ? min : delete;
					dp[i + 1][j + 1] = min;
				}
			}
		}

		return (dp[len1][len2] <= 7 && word1.contains(word2));
	}

	public void help() {
		System.out.println("=========================  Help information  =========================");
		System.out.println("\t0: help\n");
		System.out.println("=========================    User Manage     =========================");
		System.out.println("\t1: add user");
		System.out.println("\t2: edit user");
		System.out.println("\t3: delete user");
		System.out.println("\t4: dump all users\n");
		System.out.println("=========================    Book Manage     =========================");
		System.out.println("\t5: add book");
		System.out.println("\t6: edit book");
		System.out.println("\t7: delete book");
		System.out.println("\t8: dump all books");
		System.out.println("\t9: lend book");
		System.out.println("\t10: return book\n");
		System.out.println("=========================  Query information  =========================");
		System.out.println("\t11: query book");
		System.out.println("\t12: query user\n");
		System.out.println("=========================       Saving        =========================");
		System.out.println("\t14: save users and books data\n");
		System.out.println("=========================        Exit         =========================");
		System.out.println("\t15: exit\n");
	}
}
