import java.io.Serializable;

public class User implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;
	private String name;
	private String phone;
	private String email;

	public User(String name, String phone, String email) {
		this.name = name;
		this.phone = phone;
		this.email = email;
	}

	public String toString() {
		return String.format("User name: %s, phone: %s, email: %s\n", this.name, this.phone, this.email);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		User t = (User) obj;
		return (this.name.equals(t.getName()) && this.phone.equals(t.getPhone()) && this.email.equals(t.getEmail()));
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return this.name;
	}

	public String getPhone() {
		return this.phone;
	}

	public String getEmail() {
		return this.email;
	}
}
