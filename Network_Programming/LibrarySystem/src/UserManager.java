import java.util.LinkedList;

public class UserManager {
	private LinkedList<User> users;

	public UserManager() {
		this.users = new LinkedList<User>();
	}

	public User getUser(String name) {
		for (int i = 0; i < users.size(); i++)
			if (users.get(i).getName().equals(name))
				return users.get(i);
		return null;
	}

	public LinkedList<User> getUsers() {
		return this.users;
	}

	public void setUsers(LinkedList<User> users) {
		this.users = users;
	}

	public boolean add(String name, String phone, String email) {
		User t = getUser(name);
		if (t == null) {
			this.users.add(new User(name, phone, email));
			return true;
		}
		return false;
	}

	public boolean edit(String name, String phone, String email) {
		User t = getUser(name);
		if (t != null) {
			if (!t.getName().equals(name))
				t.setName(name);
			if (!t.getPhone().equals(phone))
				t.setPhone(phone);
			if (!t.getEmail().equals(email))
				t.setEmail(email);
			return true;
		}
		return false;
	}

	public boolean del(String name) {
		User t = getUser(name);
		if (t != null) {
			this.users.remove(t);
			return true;
		}
		return false;
	}

	public void query(String name) {
		User t = getUser(name);
		if (t != null) {
			System.out.print(t.toString());
		}
	}

	public String lquery(String name) {
		return "Next version";
	}

	public void dumpUsers() {
		for (int i = 0; i < users.size(); i++)
			System.out.printf("\t%d.\t%s", (i + 1), users.get(i).toString());
	}
}
