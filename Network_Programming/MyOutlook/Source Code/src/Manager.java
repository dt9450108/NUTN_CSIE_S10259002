import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;

public class Manager {
	private Vector<Mail> mails;

	public Manager() {
		this.mails = new Vector<Mail>();
	}

	public Vector<Mail> getMails() {
		return mails;
	}

	public void setMails(Vector<Mail> mails) {
		this.mails = mails;
	}

	public Mail getMail(int id) {
		for (int i = 0; i < this.mails.size(); i++)
			if (id == this.mails.get(i).getId())
				return this.mails.get(i);
		return null;
	}

	public void delete(int id) {
		int didx = search(id);
		if (didx >= 0)
			this.mails.remove(didx);
	}

	public int search(int mailid) {
		for (int i = 0; i < this.mails.size(); i++)
			if (this.mails.get(i).getId() == mailid)
				return i;
		return -1;
	}

	public int search(String date) {
		for (int i = 0; i < this.mails.size(); i++) {
			if (this.mails.get(i).getDate().equals(date)) {
				return i;
			}
		}
		return -1;
	}

	public void addMail(Mail mail) {
		Vector<Mail> tms = new Vector<Mail>();
		tms.addElement(mail);
		addMail(tms);
	}

	public void addMail(Vector<Mail> mails) {
		for (int i = 0; i < mails.size(); i++) {
			if (search(mails.get(i).getDate()) < 0) {
				this.mails.addElement(mails.get(i));
			}
		}
	}

	public void readData(String data) {
		// load mails file
		try {
			File fMails = new File(data);
			if (fMails.exists()) {
				FileInputStream fisMails = new FileInputStream(data);
				ObjectInputStream oisMails = new ObjectInputStream(fisMails);
				this.mails = (Vector<Mail>) oisMails.readObject();
				oisMails.close();
			}
		} catch (Exception e) {
			System.out.println("Manager readData ERROR");
			e.printStackTrace();
		}
	}

	public void storeData(String data) {
		// store mails file
		try {
			FileOutputStream fosMails = new FileOutputStream(data, false);
			ObjectOutputStream oosMails = new ObjectOutputStream(fosMails);
			oosMails.writeObject(this.mails);
			oosMails.close();
		} catch (Exception e) {
			System.out.println("Manager storeData ERROR");
			e.printStackTrace();
		}
	}
}
