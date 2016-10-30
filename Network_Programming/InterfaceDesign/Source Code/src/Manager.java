import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;

/**
 * 
 * @author Huang Shih-Hao
 * @class Manager
 * @properties File is the name of the .dat file; Studes is the Student Object
 *
 */
public class Manager {
	private String File;
	private Vector<Student> Studs;

	public Manager(String File) {
		this.File = File;
		this.Studs = new Vector<Student>();
	}

	// search a student by id and name
	public int search(String num, String name) {
		int l = this.Studs.size();
		for (int i = 0; i < l; i++) {
			if (num.toLowerCase().equals(this.Studs.get(i).getNum().toLowerCase()) || name.equals(this.Studs.get(i).getName())) {
				return i;
			}
		}
		return -1;
	}

	// search a student by id and name except specific student
	public int search(String num, String name, int igIdx) {
		int l = this.Studs.size();
		for (int i = 0; i < l; i++) {
			if (i != igIdx && (num.toLowerCase().equals(this.Studs.get(i).getNum().toLowerCase()) || name.equals(this.Studs.get(i).getName()))) {
				return i;
			}
		}
		return -1;
	}

	// search a student by identified number which is numbered when a student object created
	public int search(int id) {
		int l = this.Studs.size();
		for (int i = 0; i < l; i++)
			if (this.Studs.get(i).getId() == id)
				return i;
		return -1;
	}

	public boolean add(String num, String name, int sport, int course) {
		int found = search(num, name);
		if (found != -1)
			return false;
		this.Studs.addElement(new Student(num, name, sport, course));
		return true;
	}

	public boolean del(int id) {
		int found = search(id);
		if (found == -1)
			return false;
		this.Studs.removeElementAt(found);
		return true;
	}

	public boolean delete(int idx) {
		if (idx < 0 || idx > Studs.size())
			return false;
		this.Studs.removeElementAt(idx);
		return true;
	}

	public boolean edit(int idx, String num, String name, int sport, int course) {
		// make idx bounding 0 to the size of the list
		if (idx < 0 || idx > Studs.size())
			return false;

		// check if there is a student ID or name which is same as current edited student
		int found = search(num, name, idx);
		if (found != -1)
			return false;

		this.Studs.get(idx).setNum(num);
		this.Studs.get(idx).setName(name);
		this.Studs.get(idx).setSport(sport);
		this.Studs.get(idx).setCourse(course);
		return true;
	}

	public int maxId() {
		int max = 0;
		for (int i = 0; i < this.Studs.size(); i++)
			if (max < this.Studs.get(i).getId())
				max = this.Studs.get(i).getId();
		return max;
	}

	public void setFile(String File) {
		this.File = File;
	}

	public void setStuds(Vector<Student> Studs) {
		this.Studs = Studs;
	}

	public String getFile() {
		return this.File;
	}

	public Vector<Student> getStuds() {
		return this.Studs;
	}

	// read the .dat file in a serializable way
	public void readDatas(String nFile) {
		if (nFile != null) {
			this.File = nFile;
		}
		try {
			File fStuds = new File(this.File);
			if (fStuds.exists()) {
				FileInputStream fisStuds = new FileInputStream(this.File);
				ObjectInputStream oisStuds = new ObjectInputStream(fisStuds);
				this.Studs = (Vector<Student>) oisStuds.readObject();
				oisStuds.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// store the students data in a serializable way
	public void storeDatas() {
		try {
			FileOutputStream fosStuds = new FileOutputStream(this.File, false);
			ObjectOutputStream oosStuds = new ObjectOutputStream(fosStuds);
			oosStuds.writeObject(this.Studs);
			oosStuds.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
